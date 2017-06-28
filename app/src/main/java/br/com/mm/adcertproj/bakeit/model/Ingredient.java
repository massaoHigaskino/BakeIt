package br.com.mm.adcertproj.bakeit.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import timber.log.Timber;

@DatabaseTable(tableName = Ingredient.Contract.TABLE_NAME)
public class Ingredient extends AbstractModel implements Serializable {

    // region ATTRIBUTES
    public static final long serialVersionUID = 1L;

    // region PK
    @DatabaseField(generatedId = true)
    private Integer id; // TODO id may be 0, could be incompatible to SQLite _id
    // endregion PK

    // region FK
    @DatabaseField
    private Integer recipeId;
    // endregion FK

    @SerializedName(Contract.COLUMN_INGREDIENT)
    @Expose
    @DatabaseField
    private String ingredient;
    @SerializedName(Contract.COLUMN_MEASURE)
    @Expose
    @DatabaseField
    private String measure;
    @SerializedName(Contract.COLUMN_QUANTITY)
    @Expose
    @DatabaseField
    private Float quantity;
    // endregion ATTRIBUTES

    // region GETTERS & SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
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
        values.put(Contract.COLUMN_RECIPE_ID, getRecipeId());
        values.put(Contract.COLUMN_INGREDIENT, getIngredient());
        values.put(Contract.COLUMN_MEASURE, getMeasure());
        values.put(Contract.COLUMN_QUANTITY, getQuantity());
        return values;
    }

    /**
     * This method creates an array of instances of this class based on data pointed by a Cursor,
     * usually coming from a query at a Database.
     * @param cursor A Cursor pointing to data to be 'deserialized' into an array of instances of this class.
     * @return An array of instances of this class.
     */
    public static Ingredient[] listFromCursor(Cursor cursor) {
        if(cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        Ingredient[] resultArray = new Ingredient[cursor.getCount()];

        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Ingredient ingredient = new Ingredient();
            int columnIndex = cursor.getColumnIndex(Contract.COLUMN_ID);
            if(columnIndex >= 0)
                ingredient.setId(cursor.getInt(columnIndex));
            else
                Timber.e("Invalid cursor index!");
            columnIndex = cursor.getColumnIndex(Contract.COLUMN_RECIPE_ID);
            if(columnIndex >= 0)
                ingredient.setRecipeId(cursor.getInt(columnIndex));
            else
                Timber.e("Invalid cursor index!");
            columnIndex = cursor.getColumnIndex(Contract.COLUMN_INGREDIENT);
            if(columnIndex >= 0)
                ingredient.setIngredient(cursor.getString(columnIndex));
            else
                Timber.e("Invalid cursor index!");
            columnIndex = cursor.getColumnIndex(Contract.COLUMN_MEASURE);
            if(columnIndex >= 0)
                ingredient.setMeasure(cursor.getString(columnIndex));
            else
                Timber.e("Invalid cursor index!");
            columnIndex = cursor.getColumnIndex(Contract.COLUMN_QUANTITY);
            if(columnIndex >= 0)
                ingredient.setQuantity(cursor.getFloat(columnIndex));
            else
                Timber.e("Invalid cursor index!");
            resultArray[i] = ingredient;
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
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_QUANTITY = "quantity";
    }
    // endregion
}
