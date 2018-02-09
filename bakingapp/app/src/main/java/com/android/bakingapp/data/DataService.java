package com.android.bakingapp.data;

import com.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Christos on 31/01/2018.
 */

public interface DataService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
