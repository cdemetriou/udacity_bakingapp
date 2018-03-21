package com.android.bakingapp.modules.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.bakingapp.R;
import com.android.bakingapp.data.Constants;
import com.android.bakingapp.databinding.ActivityDetailBinding;
import com.android.bakingapp.model.Recipe;
import com.android.bakingapp.model.Step;
import com.android.bakingapp.modules.step.StepActivity;
import com.android.bakingapp.modules.step.StepFragment;

import java.util.ArrayList;
import java.util.List;

import static com.android.bakingapp.data.Constants.BACK_STACK_DETAIL;
import static com.android.bakingapp.data.Constants.BACK_STACK_STEP;
import static com.android.bakingapp.data.Constants.RECIPE_ID_EXTRA;
import static com.android.bakingapp.data.Constants.RECIPE_LIST_EXTRA;
import static com.android.bakingapp.data.Constants.STEP_INDEX_EXTRA;
import static com.android.bakingapp.data.Constants.STEP_LIST_EXTRA;
import static com.android.bakingapp.data.Constants.TABLET_LANDSCAPE;

/**
 * Created by Christos on 02/02/2018.
 */

public class DetailActivity extends AppCompatActivity implements RecipesDetailAdapter.OnStepClickListener, StepFragment.OnItemClickListener {

    ActivityDetailBinding binding;
    Recipe recipe = new Recipe();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RECIPE_ID_EXTRA, recipe.getId());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.toolbar.setNavigationOnClickListener(toolbarListener);

        if (savedInstanceState != null) {
            String id = savedInstanceState.getString(RECIPE_ID_EXTRA);
            ArrayList<Recipe> recipesList = getIntent().getExtras().getParcelableArrayList(RECIPE_LIST_EXTRA);
            recipe = Recipe.getWithId(recipesList, id);
        }
        else recipe = getIntent().getExtras().getParcelable(Constants.RECIPE_EXTRA);

        getSupportActionBar().setTitle(recipe.getName());

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_EXTRA, recipe);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, detailFragment)
                .addToBackStack(BACK_STACK_DETAIL).commit();

        if (binding.getRoot().getTag().equals(TABLET_LANDSCAPE)) {
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.fragment_container2, stepFragment)
                    .addToBackStack(BACK_STACK_STEP).commit();

        }

    }

    private final Toolbar.OnClickListener toolbarListener = view -> {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (binding.fragmentContainer2 == null) {
            if (fragmentManager.getBackStackEntryCount() > 1) fragmentManager.popBackStack(BACK_STACK_DETAIL, 0);
            else if (fragmentManager.getBackStackEntryCount() > 0) finish();
        }
        else finish();

    };


    @Override
    public void onItemClick(List<Step> stepsOut, int clickedItemIndex) {
        setupStep(stepsOut, clickedItemIndex);

    }


    private void setupStep(List<Step> stepsOut, int clickedItemIndex) {
        StepFragment fragment = new StepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        ArrayList<Step> steps = (ArrayList<Step>) stepsOut;
        bundle.putParcelableArrayList(STEP_LIST_EXTRA, steps);
        bundle.putInt(STEP_INDEX_EXTRA, clickedItemIndex);

        fragment.setArguments(bundle);

        if (binding.getRoot().getTag().equals(TABLET_LANDSCAPE)) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container2, fragment)
                    .addToBackStack(BACK_STACK_STEP).commit();
        }
        else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putParcelableArrayListExtra(STEP_LIST_EXTRA, steps);
            intent.putExtra(STEP_INDEX_EXTRA, clickedItemIndex);
            startActivity(intent);
        }
    }


}
