package com.android.bakingapp.modules.step;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.bakingapp.R;
import com.android.bakingapp.data.Constants;
import com.android.bakingapp.databinding.FragmentStepBinding;
import com.android.bakingapp.model.Recipe;
import com.android.bakingapp.model.Step;
import com.android.bakingapp.modules.detail.DetailActivity;
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
import java.util.List;

import static com.android.bakingapp.data.Constants.STEP_INDEX_EXTRA;
import static com.android.bakingapp.data.Constants.STEP_LIST_EXTRA;

/**
 * Created by Christos on 02/02/2018.
 */

public class StepFragment extends Fragment {


    FragmentStepBinding binding;
    private SimpleExoPlayer mExoPlayer;
    private BandwidthMeter bandwidthMeter;
    private ArrayList<Step> steps = new ArrayList<>();
    private int index;
    private Handler mainHandler;
    String recipeName;
    Step currentStep;

    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(List<Step> allSteps, int Index, String recipeName);
    }

    public StepFragment(){}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_EXTRA, steps);
        outState.putInt(STEP_INDEX_EXTRA, index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step, container, false);

        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        itemClickListener =(DetailActivity) getActivity();

        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEP_LIST_EXTRA);
            index = savedInstanceState.getInt(STEP_INDEX_EXTRA);
        }
        else {
            steps = getArguments().getParcelableArrayList(STEP_LIST_EXTRA);
            index = getArguments().getInt(STEP_INDEX_EXTRA);

            if (steps == null){
                Recipe recipe = getArguments().getParcelable(Constants.RECIPE_EXTRA);
                steps = (ArrayList<Step>) recipe.getSteps();
                index = 0;
            }
        }

        currentStep = steps.get(index);

        binding.recipeStepDetailText.setText(currentStep.getDescription());
        binding.recipeStepDetailText.setVisibility(View.VISIBLE);
        binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        if (currentStep.getThumbnailURL() != "" && currentStep.getThumbnailURL() != null) {
            String image = currentStep.getThumbnailURL();
            Uri uri = Uri.parse(image).buildUpon().build();
            Glide.with(getActivity())
                    .load(uri)
                    .into(binding.thumbImage);
        }

        if (currentStep.getVideoURL() != "" && currentStep.getVideoURL() != null) {
            String video = currentStep.getVideoURL();
            initializePlayer(Uri.parse(video));

            if (binding.getRoot().getTag() == "sw600dp-land") {
                getActivity().findViewById(R.id.fragment_container2).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }
            else if (isLandscapeMode(getContext())){
                binding.recipeStepDetailText.setVisibility(View.GONE);
            }

        }
        else {
            mExoPlayer = null;
            binding.playerView.setForeground(ContextCompat.getDrawable(getContext(), R.drawable.ic_launcher_background));
            binding.playerView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        }

        binding.nextStep.setOnClickListener(changeClickListener);
        binding.previousStep.setOnClickListener(changeClickListener);

        return binding.getRoot();
    }

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
                        itemClickListener.onItemClick(steps,Integer.valueOf(steps.get(index).getId()) + 1,recipeName);
                    }
                    else Toast.makeText(getContext(),"This is thee last step.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.previousStep:
                    if (Integer.valueOf(steps.get(index).getId()) > 0) {
                        if (mExoPlayer !=null){
                            mExoPlayer.stop();
                        }
                        itemClickListener.onItemClick(steps,Integer.valueOf(steps.get(index).getId()) - 1,recipeName);
                    }
                    else Toast.makeText(getActivity(),"This is the first step.", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.playerView.setPlayer(mExoPlayer);

            // Prepare media source
            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mExoPlayer !=null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer !=null) {
            releasePlayer();
            mExoPlayer =null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer !=null) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer !=null) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
    }

}
