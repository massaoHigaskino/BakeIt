package br.com.mm.adcertproj.bakeit.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.adapter.IngredientsAdapter;
import br.com.mm.adcertproj.bakeit.adapter.StepsAdapter;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepsFragment extends Fragment implements StepsAdapter.StepClickListener {
    // region ATTRIBUTES
    @BindView(R.id.tv_recipe_name) TextView mRecipeNameTextView;
    @BindView(R.id.tv_recipe_details) TextView mRecipeDetailsTextView;
    @BindView(R.id.rv_ingredients) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.rv_steps) RecyclerView mStepsRecyclerView;
    private OnRecipeStepClickedListener mCallback;
    private Recipe recipe;
    // endregion ATTRIBUTES

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public StepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.d("creating fragment");
        final View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, rootView);

        final Intent intent = getActivity().getIntent();
        if(intent.hasExtra(BuildConfig.EXTRA_RECIPE_KEY)) {
            try {
                recipe = (Recipe) intent.getSerializableExtra(BuildConfig.EXTRA_RECIPE_KEY);
            } catch (Throwable t) {
                Timber.e(t, "Invalid object stored with '%1$s' key. Object of type %2$s expected.",
                        BuildConfig.EXTRA_RECIPE_KEY, Recipe.class.getName());
            }
        } else if(savedInstanceState != null) {
            recipe = (Recipe) savedInstanceState.getSerializable(BuildConfig.EXTRA_RECIPE_KEY);
            savedInstanceState.clear();
        }

        if(recipe != null) {
            mRecipeNameTextView.setText(recipe.getName());
            String servingsPluralized = getResources().getQuantityString(
                    R.plurals.serving, recipe.getServings(), recipe.getServings());
            mRecipeDetailsTextView.setText(getString(R.string.servings_text, servingsPluralized));
        }

        IngredientsAdapter mIngredientsAdapter = new IngredientsAdapter(getContext(), recipe);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);

        mIngredientsRecyclerView.setLayoutManager(layoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

        StepsAdapter mStepsAdapter = new StepsAdapter(this, recipe);

        layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);

        mStepsRecyclerView.setLayoutManager(layoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BuildConfig.EXTRA_RECIPE_KEY, recipe);
    }

    /**
     * Receives the context in which this fragment is attached, useful for callbacks this fragment
     * may need to do.
     * @param context Activity which impements this fragments callbacks.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeStepClickedListener) context;
        } catch (ClassCastException e) {
            Timber.e(e, "Fragment attached to non callable context. Implement %1$s in order to" +
                    " receive callbacks from this fragment.", OnRecipeStepClickedListener.class.getName());
        }

        Timber.d("Fragment attached.");
    }

    @Override
    public void onStepClick(Step step) {
        mCallback.onRecipeStepClicked(step);
    }

    public interface OnRecipeStepClickedListener {
        void onRecipeStepClicked(Step step);
    }
}
