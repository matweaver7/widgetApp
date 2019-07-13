package com.matweaver.mike;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.String;
import java.net.*;

/**
 * Implementation of App Widget functionality.
 */
public class mikeWidget extends AppWidgetProvider {

    private String _name;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            SharedPreferences prefs = context.getSharedPreferences("mike_widget_pref", context.MODE_PRIVATE);
            String name = prefs.getString("username", "was not found");
            _name = name;

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.mike_widget);
            remoteViews.setTextViewText(R.id.userNameFilledIn, name);
            Thread thread = new Thread() {
                @Override
                public void run(){
                    try {
                        Log.d("sentMessage", "thread execution works");
                        sendRequest(mikeWidget.this.getName());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
//            Log.d("test", "this is a test");
            Intent intent = new Intent(context, mikeWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button3, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
    String getName(){
        return _name;
    }


    private void sendRequest(String name) {

        HttpURLConnection client = null;
        try {
            URL url = new URL("https://httpbin.org/post" + name );
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Content-Type", "application/json; utf-8");
            client.setRequestProperty("Accept", "application/json");
            client.setDoOutput(true);
            Log.d("messageSent", "1");
            String jsonInputString = "{\"name\": \"Upendra\", \"job\": \"Programmer\"}";
            byte[] input = jsonInputString.getBytes("utf-8");
            Log.d("messageSent", "2");
            OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
            Log.d("messageSent", "3");
            outputPost.write(input, 0, input.length);
            Log.d("messageSent", "4");

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Log.d("messageSent", response.toString());
                //System.out.println(response.toString());
            }

            outputPost.flush();
            outputPost.close();

        }
        catch(MalformedURLException error) {
            //Handles an incorrectly entered URL
            Log.d("messageSent", "Handles an incorrectly entered URL");
            Log.d("messageSent", error.getMessage());
        }
        catch(SocketTimeoutException error) {
            //Handles URL access timeout.
            Log.d("messageSent", "problem was here URL Access");
            Log.d("messageSent", error.getMessage());
        }
        catch (IOException error) {
            //Handles input and output errors
            Log.d("messageSent", "Handles input and output errors");
            Log.d("messageSent", error.getMessage());
        }
        finally {
            if(client != null) // Make sure the connection is not null.
                client.disconnect();
        }
    }
}

