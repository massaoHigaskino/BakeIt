package br.com.mm.adcertproj.bakeit.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.mm.adcertproj.bakeit.BuildConfig;
import br.com.mm.adcertproj.bakeit.R;
import br.com.mm.adcertproj.bakeit.helpers.StringHelper;
import br.com.mm.adcertproj.bakeit.model.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

// TODO implementation pending
public class DetailsFragment extends Fragment {

    //region ATTRIBUTES
    @BindView(R.id.tv_step_name) TextView mStepTextView;
    @BindView(R.id.cv_exo_player) CardView mExoPlayerCardView;
    @BindView(R.id.baked_exo_player) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.iv_step) ImageView mStepImageView;
    @BindView(R.id.tv_step_instructions) TextView mStepInstructionsTextView;
    @BindView(R.id.button_next) Button mNextButton;
    @BindView(R.id.button_prev) Button mPrevButton;

    private SimpleExoPlayer mExoPlayer;
    private MediaSource mediaSource;
    private OnClickListener callback;
    //endregion ATTRIBUTES

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callback = (OnClickListener) context;
        } catch (Throwable t) {
            Timber.e(t, "Fragment attached to non callable context. Implement %1$s in order to" +
                    " receive callbacks from this fragment.", OnClickListener.class.getName());
        }

        Timber.d("Fragment attached.");
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    public void bind(final Step step) {
        Timber.d("Binding new step.");

        // Stops current media reproduction
        if(mExoPlayer != null)
            mExoPlayer.stop();
        if(mediaSource != null)
            mediaSource.releaseSource();

        // Binds new data to this fragment's views
        mStepTextView.setText(step.getShortDescription());
        if(StringHelper.isNullOrEmpty(step.getVideoURL())
                && StringHelper.isNullOrEmpty(step.getThumbnailURL())) {
            mExoPlayerCardView.setVisibility(View.GONE);
        } else {
            mExoPlayerCardView.setVisibility(View.VISIBLE);
            if(!StringHelper.isNullOrEmpty(step.getVideoURL())) {
                initializePlayer(Uri.parse(step.getVideoURL()));
                mExoPlayerView.setVisibility(View.VISIBLE);
            } else {
                mExoPlayerView.setVisibility(View.GONE);
            }
            if(!StringHelper.isNullOrEmpty(step.getThumbnailURL())) {
                Picasso.with(getContext()).load(step.getThumbnailURL()).into(mStepImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        mStepImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        mStepImageView.setVisibility(View.GONE);
                        Timber.e("Something went wrong while loading image for uri: %1$s", step.getThumbnailURL());
                    }
                });
            } else {
                mStepImageView.setVisibility(View.GONE);
            }
        }
        mStepInstructionsTextView.setText(step.getDescription());
        if(callback!=null) {
            mNextButton.setEnabled(callback.hasNextStep());
            mPrevButton.setEnabled(callback.hasPrevStep());
            mNextButton.setVisibility(callback.hasNextStep() ? View.VISIBLE : View.INVISIBLE);
            mPrevButton.setVisibility(callback.hasPrevStep() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @OnClick(R.id.button_next)
    public void onClickNext() {
        if(callback != null) {
            callback.onClickNext();
        }
    }

    @OnClick(R.id.button_prev)
    public void onClickPrev() {
        if(callback != null) {
            callback.onClickPrev();
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);
        }

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), BuildConfig.EXO_PLAYER_USER_AGENT);
        mediaSource = new ExtractorMediaSource(mediaUri,
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    public interface OnClickListener {
        boolean hasNextStep();
        boolean hasPrevStep();
        void onClickNext();
        void onClickPrev();
    }
}
