package br.com.mm.adcertproj.bakeit.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Step.Contract.TABLE_NAME)
public class Step extends AbstractModel implements Serializable {

    // region ATTRIBUTES
    public static final long serialVersionUID = 1L;

    // region PK
    @SerializedName(Contract.JSON_ID)
    @Expose
    @DatabaseField(allowGeneratedIdInsert = true)
    private Integer id; // TODO id may be 0, could be incompatible to SQLite _id
    // endregion PK

    // region FK
    @DatabaseField
    private Integer recipeId;
    // endregion FK

    @SerializedName(Contract.COLUMN_SHORT_DESC)
    @Expose
    @DatabaseField
    private String shortDescription;
    @SerializedName(Contract.COLUMN_DESCRIPTION)
    @Expose
    @DatabaseField
    private String description;
    @SerializedName(Contract.COLUMN_VIDEO_URL)
    @Expose
    @DatabaseField
    private String videoURL;
    @SerializedName(Contract.COLUMN_THUMB_URL)
    @Expose
    @DatabaseField
    private String thumbnailURL;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
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
        values.put(Contract.COLUMN_RECIPE_ID, getRecipeId());
        values.put(Contract.COLUMN_SHORT_DESC, getShortDescription());
        values.put(Contract.COLUMN_DESCRIPTION, getDescription());
        values.put(Contract.COLUMN_VIDEO_URL, getVideoURL());
        values.put(Contract.COLUMN_THUMB_URL, getThumbnailURL());
        return values;
    }

    /**
     * This method creates an array of instances of this class based on data pointed by a Cursor,
     * usually coming from a query at a Database.
     * @param cursor A Cursor pointing to data to be 'deserialized' into an array of instances of this class.
     * @return An array of instances of this class.
     */
    public static Step[] listFromCursor(Cursor cursor) {
        if(cursor == null || cursor.getCount() <= 0) {
            return null;
        }

        Step[] resultArray = new Step[cursor.getCount()];

        for(int i = 0; i < cursor.getCount(); i++) {
            Step step = new Step();
            step.setId(cursor.getInt(cursor.getColumnIndex(Contract.COLUMN_ID)));
            step.setRecipeId(cursor.getInt(cursor.getColumnIndex(Contract.COLUMN_RECIPE_ID)));
            step.setShortDescription(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_SHORT_DESC)));
            step.setDescription(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_DESCRIPTION)));
            step.setVideoURL(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_VIDEO_URL)));
            step.setThumbnailURL(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_THUMB_URL)));
            resultArray[i] = step;
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
        public static final String COLUMN_SHORT_DESC = "shortDescription";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "videoURL";
        public static final String COLUMN_THUMB_URL = "thumbnailURL";
        public static final String JSON_ID = "id";
    }
    // endregion
}
