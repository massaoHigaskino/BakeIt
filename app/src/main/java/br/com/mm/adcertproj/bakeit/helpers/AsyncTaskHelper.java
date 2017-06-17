package br.com.mm.adcertproj.bakeit.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import br.com.mm.adcertproj.bakeit.R;

public class AsyncTaskHelper {
    // region ATTRIBUTES
    private static ProgressDialog progressDialog;
    // endregion

    // region PUBLIC METHODS
    public static void showProgressDialog(Context context) {
        dismissProgressDialog();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.progress_message);
        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
    // endregion
}
