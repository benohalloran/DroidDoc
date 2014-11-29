package com.pseudosudostudios.jdoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import data.FileInfoFactory;

/**
 * Created by Ben on 11/17/2014.
 * Copyright (c) 2014 Pseudo Sudo Studios
 */
public class DataLoader extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = DataLoader.class.getSimpleName();
    private Context context;
    private ProgressDialog dialog;
    boolean publishProgress;
    private long start;

    private final boolean LOG_ERROR = false;
    int THREAD_COUNT = 30;

    public DataLoader(Context context, ProgressDialog dialog, int style) {
        this.context = context;
        this.dialog = dialog;
        publishProgress = style == ProgressDialog.STYLE_HORIZONTAL;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        start = System.currentTimeMillis();
        final String parent = "data-files";

        try {
            final String[] files = context.getAssets().list(parent);
            final int fileCount = files.length;

            //This parsing code gave ~15 seconds for all files on HTC One M7 Android 4.4.3
            //Device had several background tasks running as well.
            ExecutorService executorService = Executors.newCachedThreadPool();
            for (int i = 0; i < fileCount; i++) {
                final int a = i;
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Thread.interrupted())
                                return;
                            String file = files[a];
                            if (Thread.interrupted())
                                return;
                            loadData(parent, file);
                            if (publishProgress)
                                publishProgress(a, fileCount);
                        } catch (Exception e) {
                            if (LOG_ERROR)
                                e.printStackTrace();
                        }
                    }
                });
            }
            executorService.shutdown(); //No more can be scheduled
            if (executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                Log.i(TAG, "Executor finished");
            } else {
                Log.w(TAG, "Executor forced stop");
                loadData(parent, "Activity.json"); //A large class on which much of the usability is based on.
                executorService.shutdownNow(); //Force termination
            }
        } catch (Exception e) {
            if (LOG_ERROR)
                e.printStackTrace();
        }
        FileInfoFactory.sortPackages(); //sort when done
        Log.i("DataLoader", "Parse time: " +
                ((System.currentTimeMillis() - start) / 1000D));
        return null;
    }

    private double loadData(String parent, String currentFile) {
        if (!currentFile.endsWith(".json"))
            return 0;
        try {
            long start = System.currentTimeMillis();
            InputStream stream =
                    context.getAssets().open(parent +
                            File.separator + currentFile);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            while (reader.ready() && !Thread.interrupted())
                buffer.append(reader.readLine());
            reader.close();
            String jString = buffer.toString();
            JSONObject jObj = new JSONObject(jString);
            FileInfoFactory.parseFile(jObj);
            double d = (System.currentTimeMillis() - start) / 1000D;
            Log.i(TAG, String.format("%s parse time: %.03f", currentFile, d));
            return d;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int total = values[1];
        dialog.setMax(total);
        dialog.incrementProgressBy(1);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.cancel();
        context.startActivity(new Intent(context, ExplorerActivity.class));
    }
}
