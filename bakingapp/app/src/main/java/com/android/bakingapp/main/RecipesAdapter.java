package com.android.bakingapp.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.model.Recipe;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christos on 02/02/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    private Context context;
    private List<Recipe> RecipesList = new ArrayList<>();
    private final OnItemClickListener listener;


    public RecipesAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Recipe item = RecipesList.get(position);
        holder.bind(item, listener);

        holder.recipe_name.setText( (item.getName() == null) ? "N/A" : item.getName() );
        holder.recipe_servings.setText((item.getServings() == null) ?  "N/A" : "Serves " + item.getServings());

        if (item.getImage() == null || item.getImage() == "") {
            Glide.with(context)
                .load(R.drawable.placeholder)
                .into(holder.placeholder);
        }
        else {
            Glide.with(context)
                .load(item.getImage())
                .into(holder.recipe_image);
        }
    }

    @Override
    public int getItemCount() {
        return RecipesList.size();
    }

    public void setRecipes(List<Recipe> newRecipes) {
        RecipesList = newRecipes;
        notifyDataSetChanged();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView recipe_image, placeholder;
        public TextView recipe_name, recipe_servings;
        public View rootView;


        public MyViewHolder(View view) {
            super(view);

            rootView = view;
            recipe_image = view.findViewById(R.id.recipe_imageview);
            recipe_name = view.findViewById(R.id.recipe_name);
            recipe_servings = view.findViewById(R.id.servings);
            placeholder = view.findViewById(R.id.placeholder);
        }

        public void bind(final Recipe item, final OnItemClickListener listener) {
            rootView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }


}