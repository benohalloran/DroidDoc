package com.pseudosudostudios.jdoc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import data.ClassInfo;
import data.FileInfoFactory;
import data.InfoObject;
import data.MethodInfo;

/**
 * Displays the details of a given class. Classes are sent through the setArguments method. The
 * arguments can either contain just the fully qualified packages, ie java.lang.Object,
 * or the package, java.lang, and the index of the corresponding object
 */
public class ClassInfoFrag extends Fragment {

    private static String PKG = "PACKAGE-KEY";
    private static String INDEX = "index";

    public static String FULL_PACKAGE = "FULL-PACKAGE";
    private InfoObject data;
    private DetailsAdapter.Type type = null;

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

    public static ClassInfoFrag newInstance(DetailsAdapter.Type type, String fullName) {
        ClassInfoFrag frag = ClassInfoFrag.newInstance(fullName);
        frag.type = type;
        return frag;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        setData(args);
    }

    public InfoObject getData() {
        return data;
    }

    private void setData(Bundle args) {
        if (args.containsKey(PKG) && args.containsKey(INDEX))
            data = FileInfoFactory.getPackagesHashMap().get(args.getString(PKG))
                    .get(args.getInt(INDEX));
        else if (args.containsKey(FULL_PACKAGE))
            data = FileInfoFactory.get(args.getString(FULL_PACKAGE));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //final View rootView = inflater.inflate(R.layout.fragment_class_info, container, false);
        final ListView rootView = new ListView(getActivity());

        if (data == null) {
            Log.i("ClassInfoFrag", "Key: " + getArguments().getString(PKG, "NOT FOUND"));
            setData(getArguments());
        }

        rootView.setAdapter(new DetailsAdapter(getActivity(), data,
                type));

        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String msg = "";
                switch (type) {
                    case METHOD:
                        msg = data.getMethods().get(i).toString();
                        break;
                    case FIELD:
                        if (data instanceof ClassInfo)
                            msg = ((ClassInfo) data).getConstants().get(i).toString();
                        break;
                    default:
                        msg = "";
                }
                Log.i("Clicked", msg);
                View popup = null;
                if (type == DetailsAdapter.Type.METHOD) {
                    popup = loadView(inflater, data.getMethods().get(i));
                }
                if (popup != null)
                    new AlertDialog.Builder(getActivity()).setCancelable(true).setView(popup)
                            .setTitle("Details").setNeutralButton("OK", null).show();
            }
        });
        return rootView;
    }

    private View loadView(LayoutInflater inflater, MethodInfo methodInfo) {
        View popup = inflater.inflate(R.layout.pop_up_details, null, false);

        TextView decName = (TextView) popup.findViewById(R.id.declared_in);
        TextView comments = (TextView) popup.findViewById(R.id.comments);
        TextView methodName = (TextView) popup.findViewById(R.id.method_string);

        if (methodInfo.fromClass == null || methodInfo.fromClass.equals(data.getFullName())) {
            decName.setVisibility(View.GONE);
            popup.findViewById(R.id.textView).setVisibility(View.GONE);
        } else
            decName.setText(methodInfo.fromClass.trim());

        if (methodInfo.comments == null || methodInfo.comments.isEmpty())
            comments.setVisibility(View.GONE);
        else
            comments.setText(methodInfo.comments.trim());

        methodName.setText(methodInfo.methodName.trim());
        return popup;
    }

}
