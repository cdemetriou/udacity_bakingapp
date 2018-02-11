package com.android.bakingapp.modules.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.bakingapp.R;
import com.android.bakingapp.widget.UpdateIngedientsService;
import com.android.bakingapp.databinding.FragmentDetailBinding;
import com.android.bakingapp.model.Ingredient;
import com.android.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.android.bakingapp.data.Constants.RECIPE_EXTRA;

/**
 * Created by Christos on 02/02/2018.
 */

public class DetailFragment extends android.support.v4.app.Fragment {

    private final static String TAG = "DetailFragment";
    FragmentDetailBinding binding;
    Recipe recipe;

    public DetailFragment(){}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_EXTRA, recipe);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE_EXTRA);
        }
        else {
            recipe = getArguments().getParcelable(RECIPE_EXTRA);
        }

        List<Ingredient> ingredients = recipe.getIngredients();

        ArrayList<String> recipeIngredientsForWidgets= new ArrayList<>();

        for (Ingredient it: ingredients) {
            binding.recipeDetailText.append("- "+ it.getIngredient()+"\n");
            binding.recipeDetailText.append("    Quantity: "+ it.getQuantity().toString()+" ");
            binding.recipeDetailText.append("    Measure: "+ it.getMeasure()+"\n\n");

            recipeIngredientsForWidgets.add(it.getIngredient()+"\n"+
                    "Quantity: "+ it.getQuantity().toString()+"\n"+
                    "Measure: "+ it.getMeasure()+"\n");
        }
        UpdateIngedientsService.startBakingService(getContext(),recipeIngredientsForWidgets);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recipeDetailRecyclerview.setLayoutManager(mLayoutManager);

        RecipesDetailAdapter adapter = new RecipesDetailAdapter(getActivity(), (DetailActivity) getActivity());
        binding.recipeDetailRecyclerview.setAdapter(adapter);
        adapter.setMasterRecipeData(recipe.getSteps());

        return binding.getRoot();
    }


}
