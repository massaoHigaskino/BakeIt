package br.com.mm.adcertproj.bakeit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.fragment.DetailsFragment;
import br.com.mm.adcertproj.bakeit.fragment.StepsFragment;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import timber.log.Timber;

public class BakeITActivity extends AppCompatActivity
        implements StepsFragment.OnRecipeStepClickedListener, DetailsFragment.OnClickListener {

    private StepsFragment mStepsFragment;
    private DetailsFragment mDetailsFragment;
    private Recipe mCurrentRecipe;
    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bake_it);

        if(savedInstanceState != null) {
            mStepsFragment = (StepsFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, BuildConfig.EXTRA_FRAG_STEP_KEY);
        }
        if(mStepsFragment == null) {
            mStepsFragment = new StepsFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frag_steps, mStepsFragment).commit();

        mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_details);

        final Intent intent = getIntent();
        if(intent.hasExtra(BuildConfig.EXTRA_RECIPE_KEY)) {
            try {
                mCurrentRecipe = (Recipe) intent.getSerializableExtra(BuildConfig.EXTRA_RECIPE_KEY);
            } catch (Throwable t) {
                Timber.e(t, "Invalid object stored with '%1$s' key. Object of type %2$s expected.",
                        BuildConfig.EXTRA_RECIPE_KEY, Recipe.class.getName());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, BuildConfig.EXTRA_FRAG_STEP_KEY, mStepsFragment);
    }

    @Override
    public void onRecipeStepClicked(Step step) {
        mCurrentStep = step;
        if(mDetailsFragment != null) {
            mDetailsFragment.bind(step);
        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(BuildConfig.EXTRA_RECIPE_KEY, mCurrentRecipe);
            intent.putExtra(BuildConfig.EXTRA_STEP_KEY, step);
            startActivity(intent);
        }
    }

    @Override
    public boolean hasNextStep() {
        return mCurrentRecipe.getSteps().indexOf(mCurrentStep) < mCurrentRecipe.getSteps().size() - 1;
    }

    @Override
    public boolean hasPrevStep() {
        return mCurrentRecipe.getSteps().indexOf(mCurrentStep) > 0;
    }

    @Override
    public void onClickNext() {
        if(hasNextStep()) {
            onRecipeStepClicked(mCurrentRecipe.getSteps()
                    .get(mCurrentRecipe.getSteps().indexOf(mCurrentStep) + 1));
        }
    }

    @Override
    public void onClickPrev() {
        if(hasPrevStep()) {
            onRecipeStepClicked(mCurrentRecipe.getSteps()
                    .get(mCurrentRecipe.getSteps().indexOf(mCurrentStep) - 1));
        }
    }
}
