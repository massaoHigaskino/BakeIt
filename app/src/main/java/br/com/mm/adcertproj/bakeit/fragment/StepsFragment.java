package br.com.mm.adcertproj.bakeit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mm.adcertproj.bakeit.R;
import timber.log.Timber;

// TODO implementation pending
public class StepsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("creating fragment");
        return inflater.inflate(R.layout.fragment_steps, container, false);
    }
}
