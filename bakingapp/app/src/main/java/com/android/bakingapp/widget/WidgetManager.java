package com.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.android.bakingapp.R;
import com.android.bakingapp.model.Recipe;

/**
 * Created by Christos on 14/03/2018.
 */

public class WidgetManager {
    private Context context;
    Recipe recipe;


    public WidgetManager(Context context) {
        this.context = context;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateWidget();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void updateWidget() {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, WidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        context.sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredient_list_view);
    }
}