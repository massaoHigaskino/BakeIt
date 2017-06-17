package br.com.mm.adcertproj.bakeit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.model.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {
    // region ATTRIBUTES
    private final Context mContext;
    private Ingredient[] mIngredients;
    // endregion ATTRIBUTES

    public IngredientsAdapter(Context context) {
        this.mContext = context;
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
        public IngredientsViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Ingredient ingredient) {}
    }
    // endregion AUXILIARY CLASSES
}
