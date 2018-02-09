package com.android.bakingapp.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.bakingapp.R;
import com.android.bakingapp.model.Step;

import java.util.List;

/**
 * Created by Christos on 02/02/2018.
 */

public class RecipesDetailAdapter extends RecyclerView.Adapter<RecipesDetailAdapter.RecyclerViewHolder> {

    List<Step> lSteps;
    private String recipeName;

    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    public RecipesDetailAdapter(Context context, OnItemClickListener itemClickListener) {
        this.context = context;
        this.onItemClickListener = itemClickListener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.recipe_detail_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textView.setText("   "+lSteps.get(position).getId()+". "+ lSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {

        return lSteps !=null ? lSteps.size():0 ;
    }

    public void setMasterRecipeData(List<Step> newSteps) {
        //lSteps = recipesIn;
        lSteps= newSteps;
        notifyDataSetChanged();
    }


    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView textView;
        View rootView;

        public RecyclerViewHolder(View view) {
            super(view);
            rootView = view;
            textView = itemView.findViewById(R.id.description);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onItemClickListener.onItemClick(lSteps,clickedPosition,recipeName);
        }
    }
}