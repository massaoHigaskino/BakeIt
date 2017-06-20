package br.com.mm.adcertproj.bakeit.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.adapter.RecipesAdapter;
import br.com.mm.adcertproj.bakeit.async.RetroBakeIT;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipesActivity extends AppCompatActivity
        implements RecipesAdapter.RecipesClickListener, RetroBakeIT.AsyncListener {

    //region ATTRIBUTES
    public static final String EXTRA_RECIPE_KEY = "br.com.mm.adcertproj.bakeit.recipe";
    @BindView(R.id.tv_error) TextView mErrorTextView;
    @BindView(R.id.rv_recipes) RecyclerView mRecipesRecyclerView;
    private RecipesAdapter mRecipesAdapter;
    //endregion ATTRIBUTES

    //region PROTECTED METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        mRecipesAdapter = new RecipesAdapter(this, this);

        int gridViewSpan = 1;
        if(screenSizeDps().x >= 600) {
            gridViewSpan = 3;
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
        Intent intent = new Intent(this, BakeITActivity.class);
        intent.putExtra(EXTRA_RECIPE_KEY, recipe);
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
        RetroBakeIT.runGetRecipesAsync(this, this);
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
