package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    // region ATTRIBUTES
    private final Context mContext;
    private final StepClickListener mClickListener;
    private Step[] mSteps;
    // endregion ATTRIBUTES

    public StepsAdapter(Context context, StepClickListener listener, Recipe recipe) {
        mContext = context;
        mClickListener = listener;
        mSteps = recipe.getSteps().toArray(new Step[recipe.getSteps().size()]);
    }

    // region PUBLIC METHODS
    public void setSteps(Step[] steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public Step[] getSteps() {
        return mSteps;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.steps_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.bind(mSteps[position]);
    }

    @Override
    public int getItemCount() {
        return mSteps != null ? mSteps.length : 0;
    }
    // endregion PUBLIC METHODS

    // region AUXILIARY CLASSES
    public interface StepClickListener {
        void onStepClick(Step step);
    }

    // TODO implementation pending
    public class StepsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step) TextView mStepTextView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_step)
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if(mClickListener != null && mSteps != null
                    && mSteps.length > adapterPosition && adapterPosition >= 0) {
                mClickListener.onStepClick(mSteps[adapterPosition]);
            }
        }

        public void bind(Step step) {
            mStepTextView.setText(step.getShortDescription());
        }
    }
    // endregion AUXILIARY CLASSES
}
