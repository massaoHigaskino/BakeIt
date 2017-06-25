package br.com.mm.adcertproj.bakeit.async;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.concurrent.Callable;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.helpers.AsyncTaskHelper;
import br.com.mm.adcertproj.bakeit.helpers.BakeITDeserializer;
import br.com.mm.adcertproj.bakeit.model.Ingredient;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import br.com.mm.adcertproj.bakeit.preferences.BakeITPreferences;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import timber.log.Timber;

import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildIngredientContentUri;
import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildRecipeContentUri;
import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildStepContentUri;

public class RetroBakeIT {

    /**
     * Using retrofit it calls a remote api waiting for an update in json format. In case of an error
     * it fallbacks to the database stored content.
     * @param context Android context used to retrieve misc. data.
     * @param listener A listener to return a deserialized Recipes array.
     */
    public static void runGetRecipesAsync(final Context context, final AsyncListener listener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BakeITPreferences.BAKE_IT_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .registerTypeAdapter(Recipe[].class, new BakeITDeserializer())
                        .create()))
                .build();

        API api = retrofit.create(API.class);
        Observable<Recipe[]> recipes = api.getRecipes(BakeITPreferences.BAKE_IT_PATH);

        recipes.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Recipe[]>() {
                    private Recipe[] recipes;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        AsyncTaskHelper.showProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull Recipe[] recipes) {
                        this.recipes = recipes;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "Failed to retrieve a new recipes.json." +
                                " Falling back to database");
                        fallbackGetRecipesAsync(context, listener);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Succesful recipes.json retrieval.");
                        persistRecipesAsync(context, recipes);
                        AsyncTaskHelper.dismissProgressDialog();
                        listener.onTaskResult(recipes);
                    }
                });
    }

    /**
     * Implements a database query in JavaRx.
     * @param context Android context used to retrieve misc. data.
     * @param listener A listener to return an array of Recipes from the database.
     */
    public static void fallbackGetRecipesAsync(final Context context, final AsyncListener listener) {
        Observable<Recipe[]> observable = Observable.fromCallable(new Callable<Recipe[]>() {
            @Override
            public Recipe[] call() throws Exception {
                return Recipe.listFromCursor(context, context.getContentResolver()
                        .query(buildRecipeContentUri(), null, null, null, null));
            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Recipe[]>() {
                    private Recipe[] recipes;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        AsyncTaskHelper.showProgressDialog(context);
                    }

                    @Override
                    public void onNext(@NonNull Recipe[] recipes) {
                        this.recipes = recipes;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "Failed to retrieve data from database");
                        AsyncTaskHelper.dismissProgressDialog();
                        Toast.makeText(context, R.string.no_data,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Succesful database retrieval.");
                        AsyncTaskHelper.dismissProgressDialog();
                        if(recipes != null && recipes.length > 0) {
                            Toast.makeText(context, R.string.unreachable_service,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, R.string.no_data,
                                    Toast.LENGTH_LONG).show();
                        }
                        listener.onTaskResult(recipes);
                    }
                });
    }

    public static void persistRecipesAsync(final Context context, final Recipe[] newRecipes) {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                Timber.d("Deleting old recipes...");

                context.getContentResolver().delete(buildIngredientContentUri(), null, null);
                context.getContentResolver().delete(buildStepContentUri(), null, null);
                context.getContentResolver().delete(buildRecipeContentUri(), null, null);

                Timber.d("Deletion completed.");
                Timber.d("Persisting new recipes...");

                for(Recipe recipe : newRecipes){
                    context.getContentResolver()
                            .insert(buildRecipeContentUri(), recipe.createContentValues());
                    for(Ingredient ingredient : recipe.getIngredients()) {
                        context.getContentResolver()
                                .insert(buildIngredientContentUri(), ingredient.createContentValues());
                    }
                    for(Step step : recipe.getSteps()) {
                        context.getContentResolver()
                                .insert(buildStepContentUri(), step.createContentValues());
                    }
                }

                Timber.d("Persistence completed.");

                return true;
            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    // region AUXILIARY CLASSES
    /**
     * Describes an remote RESTful API to Retrofit.
     */
    public interface API {
        @GET("{file}")
        Observable<Recipe[]> getRecipes(@Path("file") String filePath);
    }

    /**
     * Callback listener to return data from this class async tasks.
     */
    public interface AsyncListener {
        void onTaskResult(Recipe[] taskResultArray);
    }
    // endregion AUXILIARY CLASSES
}
