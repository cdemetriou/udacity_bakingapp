package com.android.bakingapp.modules.step;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.bakingapp.R;
import com.android.bakingapp.data.Constants;
import com.android.bakingapp.databinding.ActivityStepBinding;
import com.android.bakingapp.model.Recipe;
import com.android.bakingapp.model.Step;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import static com.android.bakingapp.data.Constants.CURRENT_WINDOW_EXTRA;
import static com.android.bakingapp.data.Constants.LARGE_LANDSCAPE;
import static com.android.bakingapp.data.Constants.PLAYBACK_POSITION_EXTRA;
import static com.android.bakingapp.data.Constants.PLAY_WHEN_READY_EXTRA;
import static com.android.bakingapp.data.Constants.STEP_INDEX_EXTRA;
import static com.android.bakingapp.data.Constants.STEP_LIST_EXTRA;

/**
 * Created by christosdemetriou on 21/03/2018.
 */

public class StepActivity extends AppCompatActivity {

    private ActivityStepBinding binding;
    private SimpleExoPlayer mExoPlayer;
    private BandwidthMeter bandwidthMeter;

    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;

    private ArrayList<Step> steps = new ArrayList<>();
    private int index;
    private Handler mainHandler;
    private Step currentStep;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_EXTRA, steps);
        outState.putInt(STEP_INDEX_EXTRA, index);

        if (mExoPlayer != null) {
            outState.putLong(PLAYBACK_POSITION_EXTRA,  mExoPlayer.getCurrentPosition());
            outState.putInt(CURRENT_WINDOW_EXTRA, mExoPlayer.getCurrentWindowIndex());
            outState.putBoolean(PLAY_WHEN_READY_EXTRA, mExoPlayer.getPlayWhenReady());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(toolbarListener);
        binding.nextStep.setOnClickListener(changeClickListener);
        binding.previousStep.setOnClickListener(changeClickListener);

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP_LIST_EXTRA);
            index = savedInstanceState.getInt(STEP_INDEX_EXTRA);

            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_EXTRA);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_EXTRA);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_EXTRA);
        }
        else {
            steps = getIntent().getExtras().getParcelableArrayList(STEP_LIST_EXTRA);
            index = getIntent().getExtras().getInt(STEP_INDEX_EXTRA);

            if (steps == null){
                Recipe recipe = getIntent().getExtras().getParcelable(Constants.RECIPE_EXTRA);
                steps = (ArrayList<Step>) recipe.getSteps();
                index = 0;
            }
        }
        setupActivity();
    }

    private final Toolbar.OnClickListener toolbarListener = view -> {
        finish();
    };

    public boolean isLandscapeMode( Context context ) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    private final Button.OnClickListener changeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.nextStep:
                    int lastIndex = steps.size()-1;
                    if (Integer.valueOf(steps.get(index).getId()) < Integer.valueOf(steps.get(lastIndex).getId())) {
                        if (mExoPlayer !=null){
                            mExoPlayer.stop();
                        }
                        resetContent(Integer.valueOf(steps.get(index).getId()) + 1);
                    }
                    else Toast.makeText(StepActivity.this,"This is the last step.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.previousStep:
                    if (Integer.valueOf(steps.get(index).getId()) > 0) {
                        if (mExoPlayer !=null){
                            mExoPlayer.stop();
                        }
                        resetContent(Integer.valueOf(steps.get(index).getId()) - 1);
                    }
                    else Toast.makeText(StepActivity.this,"This is the first step.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void resetContent(int i){
        releasePlayer();
        playbackPosition = 0;
        currentWindow = 0;
        playWhenReady = true;
        index = i;

        setupActivity();
    }

    public void setupActivity(){
        currentStep = steps.get(index);
        getSupportActionBar().setTitle(currentStep.getShortDescription());
        binding.recipeStepDetailText.setText(currentStep.getDescription());
        binding.recipeStepDetailText.setVisibility(View.VISIBLE);

        if (currentStep.getThumbnailURL() != "" && currentStep.getThumbnailURL() != null) {
            String image = currentStep.getThumbnailURL();
            Uri uri = Uri.parse(image).buildUpon().build();
            Glide.with(this)
                    .load(uri)
                    .into(binding.thumbImage);
        }

        if (currentStep.getVideoURL() != "" && currentStep.getVideoURL() != null) {
            initializePlayer();

            if (binding.getRoot().getTag() == LARGE_LANDSCAPE) {
                findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else if (isLandscapeMode(this)){
                binding.recipeStepDetailText.setVisibility(View.GONE);
            }

        }
        else {
            mExoPlayer = null;
            binding.playerView.setForeground(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background));
            binding.playerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }
    }

    private void initializePlayer() {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);

            // Prepare media source
            String userAgent = Util.getUserAgent(this, "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(currentStep.getVideoURL()), new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(playWhenReady);
            mExoPlayer.seekTo(currentWindow, playbackPosition);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        playbackPosition = mExoPlayer.getCurrentPosition();
        currentWindow = mExoPlayer.getCurrentWindowIndex();
        playWhenReady = mExoPlayer.getPlayWhenReady();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
}
