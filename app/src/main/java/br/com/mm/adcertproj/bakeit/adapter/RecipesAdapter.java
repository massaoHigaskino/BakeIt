package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.helpers.StringHelper;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    class RecipesViewHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {

        @BindView(R.id.tv_recipe_name) TextView mRecipeNameTextView;
        @BindView(R.id.iv_recipe) ImageView mRecipeImageView;

        RecipesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cv_recipe)
        void onClick() {
            int adapterPosition = getAdapterPosition();
            if(mClickListener != null && mRecipes != null
                    && mRecipes.length > adapterPosition && adapterPosition >= 0) {
                mClickListener.onRecipesClick(mRecipes[adapterPosition]);
            }
        }

        void bind(Recipe recipe) {
            mRecipeNameTextView.setText(recipe.getName());
            if(StringHelper.isNullOrEmpty(recipe.getImage())){
                mRecipeImageView.setVisibility(View.GONE);
            } else {
                Picasso.with(mContext).load(recipe.getImage()).into(mRecipeImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mRecipeImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        mRecipeImageView.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
    // endregion AUXILIARY CLASSES
}
