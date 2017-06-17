package br.com.mm.adcertproj.bakeit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.mm.adcertproj.bakeit.R;
import timber.log.Timber;

// TODO implementation pending
public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Timber.d("activity Created.");
    }
}
