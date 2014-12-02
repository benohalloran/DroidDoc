package com.pseudosudostudios.jdoc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import data.ClassInfo;
import data.FileInfoFactory;
import data.InfoObject;

public class ClassInfoActivity extends Activity {

    public static final String CLASS_INDEX = "cia_key";
    public static final String PKG = "package";

    ViewPager pager;
    InfoObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);
        pager = (ViewPager) findViewById(R.id.list_pager);

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

                data = frag.getData();


                setTitle(data.getFullName());

              /*  getFragmentManager().beginTransaction()
                        .add(R.id.container, frag)
                        .commit();*/

            }
        }
        pager.setAdapter(new SectionsPagerAdapter(getFragmentManager()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ClassInfoFrag.newInstance(DetailsAdapter.Type.values()[position],
                    data.getFullName());
        }


        @Override
        public int getCount() {
            if (data instanceof ClassInfo)
                return ((ClassInfo) data).getConstants().isEmpty() ? 1 : 2;
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Methods" : "Constants";
        }
    }


}
