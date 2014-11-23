package com.pseudosudostudios.jdoc;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import data.ClassInfo;
import data.InfoObject;

/**
 * Created by Ben on 11/12/2014.
 * Copyright (c) 2014 Pseudo Sudo Studios
 */
public class DetailsAdapter extends BaseAdapter {
    final Context context;
    final InfoObject data;
    final Type type;

    public static enum Type {
        METHOD, FIELD, CHILDREN, INTERFACE
    }

    public DetailsAdapter(Context context, InfoObject data, Type type) {
        super();
        Log.i("Details adapter", type.toString());
        this.context = context;
        this.data = data;
        this.type = type;
        assert data != null;
    }


    public int getCount() {
        switch (type) {
            case METHOD:
                return data.getMethods().size();
            case FIELD:
                if (data instanceof ClassInfo)
                    return ((ClassInfo) data).getConstants().size();
            case CHILDREN:
            case INTERFACE:
                //TODO implement CHILDREN, INTERFACE FIELD !Classinfo
                return 5;
            default:
                throw new IllegalStateException("Unkown enum: " + type);
        }
    }

    @Override
    public String getItem(int i) {
        switch (type) {
            case METHOD:
                return data.getMethods().get(i).methodName;
            case FIELD:
                if (data instanceof ClassInfo)
                    return ((ClassInfo) data).getConstants().get(i).name;
                else
                    return "FIELD INVALID";
            case CHILDREN:
            case INTERFACE:
                //TODO implement
                return type.toString();
            default:
                throw new IllegalStateException("Unkown enum: " + type);
        }
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(context);
        tv.setTextAppearance(context, android.R.style.TextAppearance_Large);
        tv.setText(getItem(i));
        return tv;
    }

}
