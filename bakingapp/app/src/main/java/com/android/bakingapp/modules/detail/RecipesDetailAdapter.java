package com.android.bakingapp.modules.detail;

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

    List<Step> stepList;
    Context context;
    private OnStepClickListener onStepClickListener;

    public interface OnStepClickListener {
        void onItemClick(List<Step> steps, int index);
    }

    public RecipesDetailAdapter(Context context, OnStepClickListener stepClickListener) {
        this.context = context;
        this.onStepClickListener = stepClickListener;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recipe_detail_item, viewGroup, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textView.setText(String.format("   %s. %s", stepList.get(position).getId(), stepList.get(position).getShortDescription()));
    }

    @Override
    public int getItemCount() {

        return stepList !=null ? stepList.size() : 0 ;
    }

    public void setMasterRecipeData(List<Step> newSteps) {
        stepList = newSteps;
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
            onStepClickListener.onItemClick(stepList, clickedPosition);
        }
    }
}