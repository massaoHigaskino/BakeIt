package br.com.mm.adcertproj.bakeit.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.adapter.RecipesAdapter;
import br.com.mm.adcertproj.bakeit.async.RetroBakeIT;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.preferences.BakeITPreferences;
import br.com.mm.adcertproj.bakeit.test.SimpleIdlingResource;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static br.com.mm.adcertproj.bakeit.service.BakeITService.startActionUpdateWidget;

public class RecipesActivity extends AppCompatActivity
        implements RecipesAdapter.RecipesClickListener, RetroBakeIT.AsyncListener {

    //region ATTRIBUTES
    @BindView(R.id.tv_error) TextView mErrorTextView;
    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    private RecipesAdapter mRecipesAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource = new SimpleIdlingResource();
    //endregion ATTRIBUTES

    //region TESTING METHODS
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
            mIdlingResource.setIdleState(false);
        }
        return mIdlingResource;
    }
    //endregion

    //region PROTECTED METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        mRecipesAdapter = new RecipesAdapter(this, this);

        int gridViewSpan = BakeITPreferences.RECIPES_GRID_WIDTH;
        if(screenSizeDps().x >= 600) {
            gridViewSpan = BakeITPreferences.RECIPES_SW600DP_GRID_WIDTH;
        }
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, gridViewSpan, GridLayoutManager.VERTICAL, false);

        mRecipesRecyclerView.setLayoutManager(layoutManager);

        mRecipesRecyclerView.setHasFixedSize(true);

        mRecipesRecyclerView.setAdapter(mRecipesAdapter);

        startRecipesTask();

        Timber.d("activity Created.");
    }
    //endregion PROTECTED METHODS

    //region PUBLIC METHODS
    @Override
    public void onRecipesClick(Recipe recipe) {
        // This preference will be used by a widget in order to get the current recipe's ingredients
        getSharedPreferences(BuildConfig.PREFERENCE_FILE_KEY, MODE_PRIVATE).edit()
                .putInt(BuildConfig.SHARED_RECIPE_KEY, recipe.getId()).apply();

        // Starts an IntentService in order to update this app's widgets
        startActionUpdateWidget(this);

        Intent intent = new Intent(this, BakeITActivity.class);
        intent.putExtra(BuildConfig.EXTRA_RECIPE_KEY, recipe);
        startActivity(intent);
    }

    @Override
    public void onTaskResult(Recipe[] taskResultArray) {
        if(taskResultArray != null && taskResultArray.length > 0) {
            showResults();
            mRecipesAdapter.setRecipes(taskResultArray);
        } else {
            showError();
        }
    }
    //endregion PUBLIC METHODS

    //region PRIVATE METHODS
    private Point screenSizeDps() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        int dpHeight = (int) (outMetrics.heightPixels / density);
        int dpWidth  = (int) (outMetrics.widthPixels / density);
        return new Point(dpWidth, dpHeight);
    }

    private void startRecipesTask() {
        RetroBakeIT.runGetRecipesAsync(this, this, mIdlingResource);
    }

    private void showResults() {
        mRecipesRecyclerView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mRecipesRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }
    //endregion PRIVATE METHODS
}
