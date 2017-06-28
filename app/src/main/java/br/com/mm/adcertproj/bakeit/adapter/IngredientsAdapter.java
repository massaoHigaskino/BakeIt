package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.helpers.StringHelper;
import br.com.mm.adcertproj.bakeit.model.Ingredient;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    // region ATTRIBUTES
    private final Context mContext;
    private Ingredient[] mIngredients;
    // endregion ATTRIBUTES

    public IngredientsAdapter(Context context, Recipe recipe) {
        this.mContext = context;
        if(recipe != null && recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            mIngredients = recipe.getIngredients().toArray(new Ingredient[recipe.getIngredients().size()]);
        }
    }

    // region PUBLIC METHODS
    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.ingredients_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutId, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.bind(mIngredients[position]);
    }

    @Override
    public int getItemCount() {
        return mIngredients != null ? mIngredients.length : 0;
    }
    // endregion PUBLIC METHODS

    // region AUXILIARY CLASSES
    class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient) TextView mIngredientTextView;

        IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Ingredient ingredient) {
            mIngredientTextView.setText(StringHelper.buildIngredientString(mContext, ingredient));
        }
    }
    // endregion AUXILIARY CLASSES
}
