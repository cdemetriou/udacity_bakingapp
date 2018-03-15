package com.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.bakingapp.R;
import com.android.bakingapp.model.Ingredient;
import com.android.bakingapp.model.Recipe;

import java.util.ArrayList;

import static com.android.bakingapp.MyApplication.getWidgetManager;

/**
 * Created by Christos on 14/03/2018.
 */

public class RemoteViewsAdapter extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsViewsFactory(this.getApplicationContext());
    }

    class IngredientsViewsFactory implements RemoteViewsFactory {
        Context context;
        ArrayList<Ingredient> ingredients = new ArrayList<>();


        public IngredientsViewsFactory(Context context) {
            this.context = context;
            Recipe recipe = getWidgetManager().getRecipe();
            if (recipe != null) ingredients = (ArrayList<Ingredient>) recipe.getIngredients();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_item);
            views.setTextViewText(R.id.ingredient_text_view, ingredients.get(position).getIngredient());
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
