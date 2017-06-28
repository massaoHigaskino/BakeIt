package br.com.mm.adcertproj.bakeit.helpers;

import android.content.Context;

import java.util.Locale;

import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.model.Ingredient;

public class StringHelper {

    private StringHelper() {

    }

    // region PUBLIC METHODS
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static String buildIngredientString(Context mContext, Ingredient ingredient) {
        String quantity = mContext.getResources().getQuantityString(
                getPluralsMeasureUnit(ingredient),
                (int) Math.ceil(ingredient.getQuantity()),
                simpleFormat(ingredient.getQuantity()));
        return mContext.getString(R.string.format_ingredient, quantity, ingredient.getIngredient());
    }
    // endregion

    // region PRIVATE METHODS
    private static String simpleFormat(double d)
    {
        if(d == (long) d)
            return String.format(Locale.getDefault(), "%d", (long)d);
        else
            return String.format(Locale.getDefault(), "%s", d);
    }

    private static int getPluralsMeasureUnit(Ingredient ingredient) {
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
    // endregion PRIVATE METHODS
}
