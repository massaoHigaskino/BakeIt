package br.com.mm.adcertproj.bakeit.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import br.com.mm.adcertproj.bakeit.helpers.DatabaseHelper;
import br.com.mm.adcertproj.bakeit.model.AbstractModel;
import br.com.mm.adcertproj.bakeit.model.Ingredient;
import br.com.mm.adcertproj.bakeit.model.Recipe;
import br.com.mm.adcertproj.bakeit.model.Step;
import timber.log.Timber;

public class BakeITContentProvider extends ContentProvider {
    public static final String AUTHORITY_NAME = "br.com.mm.adcertproj.bakeit";
    public static final String RECIPE_QUERY_PATH = "recipes";
    public static final String RECIPE_ID_PATH = "recipes/#";
    public static final String INGREDIENT_QUERY_PATH = "ingredients";
    public static final String INGREDIENT_R_ID_PATH = "ingredients/#";
    public static final String STEP_QUERY_PATH = "steps";
    public static final String STEP_R_ID_PATH = "steps/#";

    public static final int RECIPE_QUERY_CODE = 100;
    public static final int RECIPE_ID_CODE = 101;
    public static final int INGREDIENT_QUERY_CODE = 200;
    public static final int INGREDIENT_R_ID_CODE = 201;
    public static final int STEP_QUERY_CODE = 300;
    public static final int STEP_R_ID_CODE = 301;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private DatabaseHelper dbHelper;

    static {
        uriMatcher.addURI(AUTHORITY_NAME, RECIPE_QUERY_PATH, RECIPE_QUERY_CODE);
        uriMatcher.addURI(AUTHORITY_NAME, RECIPE_ID_PATH, RECIPE_ID_CODE);
        uriMatcher.addURI(AUTHORITY_NAME, INGREDIENT_QUERY_PATH, INGREDIENT_QUERY_CODE);
        uriMatcher.addURI(AUTHORITY_NAME, INGREDIENT_R_ID_PATH, INGREDIENT_R_ID_CODE);
        uriMatcher.addURI(AUTHORITY_NAME, STEP_QUERY_PATH, STEP_QUERY_CODE);
        uriMatcher.addURI(AUTHORITY_NAME, STEP_R_ID_PATH, STEP_R_ID_CODE);
    }

    @Override
    public boolean onCreate() {
        dbHelper = AbstractModel.getHelper(getContext());
        final boolean successful = dbHelper != null;
        if(successful) {
            Timber.d("Content Provider creation successful");
        } else {
            Timber.w("Content Provider creation FAILED");
        }
        return successful;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String table;
        selection = null;
        selectionArgs = null;
        switch (uriMatcher.match(uri)) {
            case RECIPE_QUERY_CODE:
                table = Recipe.Contract.TABLE_NAME;
                break;
            case RECIPE_ID_CODE:
                table = Recipe.Contract.TABLE_NAME;
                selection = Recipe.Contract.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case INGREDIENT_QUERY_CODE:
                table = Ingredient.Contract.TABLE_NAME;
                break;
            case INGREDIENT_R_ID_CODE:
                table = Ingredient.Contract.TABLE_NAME;
                selection = Ingredient.Contract.COLUMN_RECIPE_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case STEP_QUERY_CODE:
                table = Step.Contract.TABLE_NAME;
                break;
            case STEP_R_ID_CODE:
                table = Step.Contract.TABLE_NAME;
                selection = Step.Contract.COLUMN_RECIPE_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                Timber.d("URI matching failed for: %1$s", uri.toString());
                return null;
        }

        Timber.d("Uri matchi successful, executing query.");

        return dbHelper.getReadableDatabase().query(table, null, selection, selectionArgs, null, null, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Timber.d("No type defined");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if(values == null) return null;
        String table;
        String path;
        String id;
        switch (uriMatcher.match(uri)) {
            case RECIPE_QUERY_CODE:
                table = Recipe.Contract.TABLE_NAME;
                path = RECIPE_QUERY_PATH;
                id = Recipe.Contract.COLUMN_ID;
                break;
            case INGREDIENT_QUERY_CODE:
                table = Ingredient.Contract.TABLE_NAME;
                path = INGREDIENT_QUERY_PATH;
                id = Ingredient.Contract.COLUMN_RECIPE_ID;
                break;
            case STEP_QUERY_CODE:
                table = Step.Contract.TABLE_NAME;
                path = STEP_QUERY_PATH;
                id = Step.Contract.COLUMN_RECIPE_ID;
                break;
            default:
                return null;
        }

        Uri result = Uri.parse("content://" + AUTHORITY_NAME + "/" + path + "/" + values.get(id));

        boolean success = dbHelper.getWritableDatabase().insert(table, null, values) > 0;

        if(success) {
            return result;
        } else {
            return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table;
        selection = "1";
        selectionArgs = null;
        switch (uriMatcher.match(uri)) {
            case RECIPE_QUERY_CODE:
                table = Recipe.Contract.TABLE_NAME;
                break;
            case RECIPE_ID_CODE:
                table = Recipe.Contract.TABLE_NAME;
                selection = Recipe.Contract.COLUMN_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case INGREDIENT_QUERY_CODE:
                table = Ingredient.Contract.TABLE_NAME;
                break;
            case INGREDIENT_R_ID_CODE:
                table = Ingredient.Contract.TABLE_NAME;
                selection = Ingredient.Contract.COLUMN_RECIPE_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            case STEP_QUERY_CODE:
                table = Step.Contract.TABLE_NAME;
                break;
            case STEP_R_ID_CODE:
                table = Step.Contract.TABLE_NAME;
                selection = Step.Contract.COLUMN_RECIPE_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                return 0;
        }

        return dbHelper.getWritableDatabase().delete(table, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    // region CONTENT PROVIDER URI BUILDERS
    public static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY_NAME);
    public static Uri buildRecipeIdContentUri(Integer recipeId) {
        return buildRecipeContentUri().buildUpon()
                .appendPath(recipeId.toString()).build();
    }

    public static Uri buildRecipeContentUri() {
        return BASE_CONTENT_URI.buildUpon()
                .appendPath(RECIPE_QUERY_PATH)
                .build();
    }

    public static Uri buildIngredientByRecipeIdContentUri(Integer recipeId) {
        return buildIngredientContentUri().buildUpon()
                .appendPath(recipeId.toString())
                .build();
    }

    public static Uri buildIngredientContentUri() {
        return BASE_CONTENT_URI.buildUpon()
                .appendPath(INGREDIENT_QUERY_PATH)
                .build();
    }

    public static Uri buildStepByRecipeIdContentUri(Integer recipeId) {
        return buildStepContentUri().buildUpon()
                .appendPath(recipeId.toString())
                .build();
    }

    public static Uri buildStepContentUri() {
        return BASE_CONTENT_URI.buildUpon()
                .appendPath(STEP_QUERY_PATH)
                .build();
    }
    // endregion CONTENT PROVIDER URI BUILDERS
}
