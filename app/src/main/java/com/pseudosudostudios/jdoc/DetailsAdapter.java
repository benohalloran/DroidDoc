package com.pseudosudostudios.jdoc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Arrays;

import data.ClassInfo;
import data.InfoObject;
import data.MethodInfo;

public class DetailsAdapter extends BaseAdapter {
    private final Context context;
    private final InfoObject data;
    private final Type type;

    public static enum Type {
        METHOD, FIELD
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
            default:
                throw new IllegalStateException("Unknown enum: " + type);
        }
    }

    @Override
    public String getItem(int i) {
        switch (type) {
            case METHOD:
                MethodInfo methodInfo = data.getMethods().get(i);

                return methodInfo.methodName + Arrays.toString(methodInfo.args.toArray()).replace('[',
                        '(').replace(']', ')');
            case FIELD:
                if (data instanceof ClassInfo)
                    return ((ClassInfo) data).getConstants().get(i).name;
                else
                    return "FIELD INVALID";
            default:
                throw new IllegalStateException("Unknown enum: " + type);
        }
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = (TextView) LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        //TextView tv = new TextView(context);
        //tv.setTextAppearance(context, android.R.style.TextAppearance_Large);
        tv.setText(getItem(i));
        return tv;
    }

}
