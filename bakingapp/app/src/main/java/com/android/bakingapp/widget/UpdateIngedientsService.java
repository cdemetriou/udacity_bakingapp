package com.android.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Christos on 09/02/2018.
 */

public class UpdateIngedientsService extends IntentService {

    public static String FROM_ACTIVITY_INGREDIENTS_LIST ="FROM_ACTIVITY_INGREDIENTS_LIST";

    public UpdateIngedientsService() {
        super("UpdateBakingService");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateIngedientsService.class);
        intent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientsList = intent.getExtras().getStringArrayList(FROM_ACTIVITY_INGREDIENTS_LIST);

            Intent newIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE_INGREDIENTS");
            newIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE_INGREDIENTS");
            newIntent.putExtra(FROM_ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
            sendBroadcast(newIntent);
        }
    }
}
