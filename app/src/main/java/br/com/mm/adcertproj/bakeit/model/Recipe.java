package br.com.mm.adcertproj.bakeit.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildIngredientByRecipeIdContentUri;
import static br.com.mm.adcertproj.bakeit.provider.BakeITContentProvider.buildStepByRecipeIdContentUri;

@DatabaseTable(tableName = Recipe.Contract.TABLE_NAME)
public class Recipe extends AbstractModel implements Serializable {

    // region ATTRIBUTES
    public static final long serialVersionUID = 1L;

    // region PK
    @SerializedName(Contract.JSON_ID)
    @Expose
    @DatabaseField(id = true)
    private Integer id;
    // endregion PK

    @SerializedName(Contract.COLUMN_NAME)
    @Expose
    @DatabaseField
    private String name;
    @SerializedName(Contract.COLUMN_SERVINGS)
    @Expose
    @DatabaseField
    private Integer servings;
    @SerializedName(Contract.COLUMN_IMAGE)
    @Expose
    @DatabaseField
    private String image;

    // region MAPPINGS
    @SerializedName(Contract.JSON_INGREDIENTS)
    @Expose
    private List<Ingredient> ingredients;
    @SerializedName(Contract.JSON_STEPS)
    @Expose
    private List<Step> steps;
    // endregion MAPPINGS
    // endregion ATTRIBUTES

    // region GETTERS & SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
    // endregion GETTERS & SETTERS

    // region PUBLIC METHODS
    /**
     * This method uses this object's attributes to build a ContentValues, giving an easy way to
     * persist this instance into a Database.
     * @return ContentValues usually used to persist an instance into a Database.
     */
    public ContentValues createContentValues() {
        ContentValues values = new ContentValues(4);
        values.put(Contract.COLUMN_ID, getId());
        values.put(Contract.COLUMN_NAME, getName());
        values.put(Contract.COLUMN_SERVINGS, getServings());
        values.put(Contract.COLUMN_IMAGE, getImage());
        return values;
    }

    /**
     * This method creates an array of instances of this class based on data pointed by a Cursor,
     * usually coming from a query at a Database.
     * @param cursor A Cursor pointing to data to be 'deserialized' into an array of instances of this class.
     * @param context Android's context used to retrieve extra data from a content provider.
     * @return An array of instances of this class.
     */
    public static Recipe[] listFromCursor(Context context, Cursor cursor) {
        if(cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        Recipe[] resultArray = new Recipe[cursor.getCount()];

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Recipe recipe = new Recipe();
            recipe.setId(cursor.getInt(cursor.getColumnIndex(Contract.COLUMN_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_NAME)));
            recipe.setServings(cursor.getInt(cursor.getColumnIndex(Contract.COLUMN_SERVINGS)));
            recipe.setImage(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_IMAGE)));

            // TODO find some kind of lazy loading
            recipe.setIngredients(Arrays.asList(
                    Ingredient.listFromCursor(context.getContentResolver().query(
                            buildIngredientByRecipeIdContentUri(recipe.getId()), null, null, null, null)
                    )));
            recipe.setSteps(Arrays.asList(
                    Step.listFromCursor(context.getContentResolver().query(
                            buildStepByRecipeIdContentUri(recipe.getId()), null, null, null, null)
                    )));

            resultArray[i] = recipe;
        }

        cursor.close();

        return resultArray;
    }
    // endregion PUBLIC METHODS

    // region NESTED CLASSES
    /**
     * A set of names used for table, column and json naming.
     * Json element names and table/column names should be the same to avoid confusion.
     */
    public class Contract {
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_IMAGE = "image";
        public static final String JSON_ID = "id";
        public static final String JSON_INGREDIENTS = "ingredients";
        public static final String JSON_STEPS = "steps";
    }
    // endregion
}
