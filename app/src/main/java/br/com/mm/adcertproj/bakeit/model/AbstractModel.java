package br.com.mm.adcertproj.bakeit.model;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import br.com.mm.adcertproj.bakeit.helpers.DatabaseHelper;

public abstract class AbstractModel {
    /**
     * Returns a DatabaseHelper instance, which allows reading from and writing to the Database.
     * @param context Android context (base, activity, etc...)
     * @return DatabaseHelper instance.
     */
    // TODO at production level use-cases and examples would be desirable.
    public static DatabaseHelper getHelper(Context context) {
        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }
}
