package br.com.mm.adcertproj.bakeit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.fragment.DetailsFragment;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import timber.log.Timber;

public class StepDetailsActivity extends AppCompatActivity implements DetailsFragment.OnClickListener {

    private DetailsFragment mDetailsFragment;
    private Recipe mCurrentRecipe;
    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        mDetailsFragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_details);

        Intent intent = getIntent();
        if(intent.hasExtra(BuildConfig.EXTRA_RECIPE_KEY)) {
            mCurrentRecipe = (Recipe) intent.getSerializableExtra(BuildConfig.EXTRA_RECIPE_KEY);
        }
        if(intent.hasExtra(BuildConfig.EXTRA_STEP_KEY)) {
            mCurrentStep = (Step) intent.getSerializableExtra(BuildConfig.EXTRA_STEP_KEY);
        }

        mDetailsFragment.bind(mCurrentStep);

        Timber.d("Activity created.");
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
            mCurrentStep = mCurrentRecipe.getSteps()
                    .get(mCurrentRecipe.getSteps().indexOf(mCurrentStep) + 1);
            mDetailsFragment.bind(mCurrentStep);
        }
    }

    @Override
    public void onClickPrev() {
        if(hasPrevStep()) {
            mCurrentStep = mCurrentRecipe.getSteps()
                    .get(mCurrentRecipe.getSteps().indexOf(mCurrentStep) - 1);
            mDetailsFragment.bind(mCurrentStep);
        }
    }
}
