package com.matweaver.mike;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import java.lang.String;

/**
 * Implementation of App Widget functionality.
 */
public class mikeWidget extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            SharedPreferences prefs = context.getSharedPreferences("mike_widget_pref", context.MODE_PRIVATE);
            String name = prefs.getString("username", "was not found");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.mike_widget);
            remoteViews.setTextViewText(R.id.userNameFilledIn, name);

            Intent intent = new Intent(context, mikeWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button3, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}

