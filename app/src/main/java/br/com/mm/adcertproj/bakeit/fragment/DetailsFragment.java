package br.com.mm.adcertproj.bakeit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.mm.adcertproj.bakeit.R;
import butterknife.ButterKnife;
import timber.log.Timber;

// TODO implementation pending
public class DetailsFragment extends Fragment {
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("creating fragment");
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
}
