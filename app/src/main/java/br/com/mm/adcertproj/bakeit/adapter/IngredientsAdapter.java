package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import br.com.mm.adcertproj.bakeit.R;
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
    public void setIngredients(Ingredient[] ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public Ingredient[] getIngredients() {
        return mIngredients;
    }

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
    // TODO implementation pending
    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient) TextView mIngredientTextView;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Ingredient ingredient) {
            String quantity = mContext.getResources().getQuantityString(
                    getPluralsMeasureUnit(ingredient),
                    (int) Math.ceil(ingredient.getQuantity()),
                    simpleFormat(ingredient.getQuantity()));
            mIngredientTextView.setText(mContext.getString(R.string.format_ingredient,
                    quantity, ingredient.getIngredient()));
        }

        private String simpleFormat(double d)
        {
            if(d == (long) d)
                return String.format(Locale.getDefault(), "%d", (long)d);
            else
                return String.format(Locale.getDefault(), "%s", d);
        }

        private int getPluralsMeasureUnit(Ingredient ingredient) {
            int resId = -1;
            switch(ingredient.getMeasure()) {
                case "UNIT":
                    resId = R.plurals.unit;
                    break;
                case "OZ":
                    resId = R.plurals.oz;
                    break;
                case "K":
                    resId = R.plurals.k;
                    break;
                case "G":
                    resId = R.plurals.g;
                    break;
                case "CUP":
                    resId = R.plurals.cup;
                    break;
                case "TBLSP":
                    resId = R.plurals.tblsp;
                    break;
                case "TSP":
                    resId = R.plurals.tsp;
                    break;
            }
            return resId;
        }
    }
    // endregion AUXILIARY CLASSES
}
