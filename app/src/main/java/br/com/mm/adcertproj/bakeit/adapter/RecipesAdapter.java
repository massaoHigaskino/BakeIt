package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.model.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {
    // region ATTRIBUTES
    private final Context mContext;
    private final RecipesClickListener mClickListener;
    private Recipe[] mRecipes;
    // endregion ATTRIBUTES

    public RecipesAdapter(Context context, RecipesClickListener listener) {
        mContext = context;
        mClickListener = listener;
    }

    // region PUBLIC METHODS
    public void setRecipes(Recipe[] recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public Recipe[] getRecipes() {
        return mRecipes;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipes_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        holder.bind(mRecipes[position]);
    }

    @Override
    public int getItemCount() {
        return mRecipes != null ? mRecipes.length : 0;
    }
    // endregion PUBLIC METHODS

    // region AUXILIARY CLASSES
    public interface RecipesClickListener {
        void onRecipesClick(Recipe recipe);
    }

    // TODO implementation pending
    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public RecipesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if(mClickListener != null && mRecipes != null
                    && mRecipes.length > adapterPosition && adapterPosition >= 0) {
                mClickListener.onRecipesClick(mRecipes[adapterPosition]);
            }
        }

        public void bind(Recipe recipe) {}
    }
    // endregion AUXILIARY CLASSES
}
