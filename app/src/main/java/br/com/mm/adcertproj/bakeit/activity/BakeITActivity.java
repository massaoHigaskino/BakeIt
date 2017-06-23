package br.com.mm.adcertproj.bakeit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.fragment.DetailsFragment;
import br.com.mm.adcertproj.bakeit.fragment.StepsFragment;
import br.com.mm.adcertproj.bakeit.model.Step;

public class BakeITActivity extends AppCompatActivity
        implements StepsFragment.OnRecipeStepClickedListener {

    private StepsFragment mStepsFragment;
    private DetailsFragment mDetailsFragment;
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
            intent.putExtra(BuildConfig.EXTRA_STEP_KEY, step);
            startActivity(intent);
        }
    }
}
