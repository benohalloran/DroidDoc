package com.pseudosudostudios.jdoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.NumberFormat;


public class SplashActivity extends Activity {
    static final int STYLE = ProgressDialog.STYLE_HORIZONTAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(STYLE);
        dialog.setCancelable(false);
        dialog.setProgressPercentFormat(NumberFormat.getPercentInstance());
        dialog.setTitle("Loading data");
        dialog.show();
        new DataLoader(this, dialog, STYLE).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
