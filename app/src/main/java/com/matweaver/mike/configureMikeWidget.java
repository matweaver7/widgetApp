package com.matweaver.mike;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class configureMikeWidget extends Activity {

    private String username;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.configuration_activity);
        setResult(RESULT_CANCELED);
        Button setupWidget = findViewById(R.id.configSubmitBtn);
        setupWidget.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = getApplicationContext();
                LinearLayout mainLayout = findViewById(R.id.configLayout);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                EditText mEdit = findViewById(R.id.configUsernameText);
                username = mEdit.getText().toString();

                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);

                int duration = Toast.LENGTH_SHORT;
                CharSequence message = "Username: "+ username + " submitted.";
                Toast.makeText(context, message, duration).show();
                SharedPreferences prefs = context.getSharedPreferences("mike_widget_pref", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username", username);
                editor.apply();

                handleSetupWidget();
            }
        });
    }

    private void handleSetupWidget() {
//        showAppWidget();
        setContentView(R.layout.mike_widget);
        TextView text = findViewById(R.id.userNameFilledIn);
        text.setText(username);
        showAppWidget();

    }

    int appWidgetId;

    private void showAppWidget() {

        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        //Retrieve the App Widget ID from the Intent that launched the Activity//

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // If the intent does not have a widget ID, then call finish()//

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }

            //TO DO, Perform the configuration and get an instance of the AppWidgetManager//


            //Create the return intent//

            Intent resultValue = new Intent();

            //Pass the original appWidgetId//

            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            //Set the results from the ‘Configure’ Activity//

            setResult(RESULT_OK, resultValue);

            //Finish the Activity//

            finish();
        }

    }






}
