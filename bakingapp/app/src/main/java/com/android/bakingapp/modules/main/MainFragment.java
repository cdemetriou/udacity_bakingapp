package com.android.bakingapp.modules.main;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bakingapp.MyApplication;
import com.android.bakingapp.R;
import com.android.bakingapp.data.Constants;
import com.android.bakingapp.databinding.FragmentMainBinding;
import com.android.bakingapp.model.Recipe;
import com.android.bakingapp.modules.detail.DetailActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import static com.android.bakingapp.data.Constants.PHONE_LANDSCAPE;
import static com.android.bakingapp.data.Constants.RECIPE_LIST_EXTRA;

/**
 * Created by Christos on 26/01/2018.
 */

public class MainFragment extends Fragment {


    private final static String TAG = "MainFragment";
    private FragmentMainBinding binding;
    private RecipesAdapter recipesAdapter;
    private MainViewModel viewModel;
    private ArrayList<Recipe> recipeList = new ArrayList<>();

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    public MainFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        recipesAdapter =new RecipesAdapter(getActivity(), recipeListener);
        binding.recipeRecyclerview.setAdapter(recipesAdapter);

        if (binding.mainFragmentRoot.getTag().equals(PHONE_LANDSCAPE)){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
            binding.recipeRecyclerview.setLayoutManager(gridLayoutManager);
        }
        else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.recipeRecyclerview.setLayoutManager(linearLayoutManager);
        }

        return binding.getRoot();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);

        viewModel.init();
        viewModel.getRecipes()
                .observe(this, recipes -> {
                    recipeList = (ArrayList<Recipe>) recipes;
                    recipesAdapter.setRecipes(recipes);
                });
    }

    private final RecipesAdapter.OnItemClickListener recipeListener = (Recipe recipe) -> {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        detailIntent.putExtra(Constants.RECIPE_EXTRA, recipe);
        detailIntent.putParcelableArrayListExtra(RECIPE_LIST_EXTRA, recipeList);
        startActivity(detailIntent);
    };
}
