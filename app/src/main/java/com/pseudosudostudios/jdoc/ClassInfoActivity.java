package com.pseudosudostudios.jdoc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import data.FileInfoFactory;


public class ClassInfoActivity extends Activity {

    public static final String CLASS_INDEX = "cia_key";
    public static final String PKG = "package";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            ClassInfoFrag frag;
            if (extras != null) {
                if (extras.containsKey(CLASS_INDEX) && extras.containsKey(PKG)) {
                    int cl = getIntent().getExtras().getInt(CLASS_INDEX);
                    int pk = getIntent().getExtras().getInt(PKG);
                    String key = FileInfoFactory.getPackages().get(pk);
                    frag = ClassInfoFrag.newInstance(key, cl);
                } else if (extras.containsKey(ClassInfoFrag.FULL_PACKAGE))
                    frag = ClassInfoFrag.newInstance(extras.getString(ClassInfoFrag.FULL_PACKAGE));
                else
                    frag = new ClassInfoFrag();

                getFragmentManager().beginTransaction()
                        .add(R.id.container, frag)
                        .commit();

            }


        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
