package com.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

/**
 * Created by Christos on 09/02/2018.
 */

public class GridWidgetService extends RemoteViewsService {

    public List<String> remoteViewingredientsList;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridFactory(this.getApplicationContext(),intent);

    }

    class GridFactory implements RemoteViewsService.RemoteViewsFactory {

        Context context;

        public GridFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteViewingredientsList = BakingAppWidget.ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewingredientsList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(this.context.getPackageName(), R.layout.widget_item);
            views.setTextViewText(R.id.grid_item_text, remoteViewingredientsList.get(i));
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.grid_item_text, fillInIntent);
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
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
