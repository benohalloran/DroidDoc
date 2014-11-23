package com.pseudosudostudios.jdoc;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import data.FileInfoFactory;
import data.InfoObject;

/**
 * Created by Ben on 11/12/2014.
 * Copyright (c) 2014 Pseudo Sudo Studios
 */
public class ClassInfoFrag extends Fragment {

    public static String PKG = "KEWY";
    public static String INDEX = "index";

    public static String FULL_PACKAGE = "FULLPKG";
    private InfoObject data;

    public ClassInfoFrag() {
        super();
    }

    public static ClassInfoFrag newInstance(String key, int ind) {
        ClassInfoFrag f = new ClassInfoFrag();
        Bundle args = new Bundle();
        args.putString(PKG, key);
        args.putInt(INDEX, ind);
        f.setArguments(args);
        return f;
    }

    public static ClassInfoFrag newInstance(String full) {
        ClassInfoFrag f = new ClassInfoFrag();
        Bundle args = new Bundle();
        args.putString(FULL_PACKAGE, full);
        f.setArguments(args);
        return f;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        setData();
    }

    private void setData() {
        Bundle args = getArguments();
        if (args.containsKey(PKG) && args.containsKey(INDEX))
            data = FileInfoFactory.getPackagesHashMap().get(args.getString(PKG))
                    .get(args.getInt(INDEX));
        else if (args.containsKey(FULL_PACKAGE))
            data = FileInfoFactory.get(args.getString(FULL_PACKAGE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class_info, container, false);
        if (data == null) {
            Log.i("ClassInfoFrag", "Key: " + getArguments().getString(PKG, "NOT FOUND"));
            setData();
        }
        if (data != null) {
            TextView tv = (TextView) rootView.findViewById(R.id.class_info_name);
            tv.setText(data.getFullName());

            ListView methods = (ListView) rootView.findViewById(R.id.methods_list);
            ListView fields = (ListView) rootView.findViewById(R.id.fields_list);
            ListView children = (ListView) rootView.findViewById(R.id.children_list);
            ListView interfaces = (ListView) rootView.findViewById(R.id.interfaces_list);

            //METHOD, FIELD, CHILDREN, INTERFACE
            ListView[] views = {methods, fields, children, interfaces};
            for (int i = 0; i < views.length; i++) {
                views[i].setAdapter(new DetailsAdapter(getActivity(), data,
                        DetailsAdapter.Type.values()[i]));
            }

        }
        return rootView;
    }

}
