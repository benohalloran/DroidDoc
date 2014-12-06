package com.pseudosudostudios.jdoc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

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
        dialog.setTitle("Loading Data");
        dialog.show();
        new DataLoader(this, dialog, STYLE).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
