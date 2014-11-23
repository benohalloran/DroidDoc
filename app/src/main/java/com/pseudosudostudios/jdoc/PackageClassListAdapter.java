package com.pseudosudostudios.jdoc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import data.FileInfoFactory;
import data.InfoObject;

/**
 * Created by Ben on 11/17/2014.
 * Copyright (c) 2014 Pseudo Sudo Studios
 */
public class PackageClassListAdapter extends BaseAdapter{
    private List<InfoObject> data;
    private Context context;

    public PackageClassListAdapter(Context c, String pkg) {
        context = c;
        data = FileInfoFactory.getPackagesHashMap().get(pkg);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(context);
        tv.setTextAppearance(context, android.R.style.TextAppearance_Large);
        InfoObject info = (InfoObject) getItem(i);
        tv.setText(info.getName());
        return tv;
    }
}
