package com.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.bakingapp.R;
import com.android.bakingapp.model.Recipe;

import static com.android.bakingapp.MyApplication.getWidgetManager;


/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_main);
        Recipe recipe = getWidgetManager().getRecipe();

        Intent ingredientListWidgetIntent = new Intent(context, RemoteViewsAdapter.class);
        views.setRemoteAdapter(R.id.ingredient_list_view, ingredientListWidgetIntent);

        if (recipe != null) views.setTextViewText(R.id.recipe_title_text_view, recipe.getName());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {}

    @Override
    public void onDisabled(Context context) {}

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}