package com.pseudosudostudios.jdoc;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import data.FileInfoFactory;

/**
 * Created by Ben on 11/9/2014.
 * Copyright (c) 2014 Pseudo Sudo Studios
 */
public class PackageListFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PackageListFragment newInstance(int sectionNumber) {
        PackageListFragment fragment = new PackageListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PackageListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.package_list, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.package_list);
        list.setAdapter(new PackageClassListAdapter(getActivity(),
                FileInfoFactory.getPackage(getArguments().getInt(ARG_SECTION_NUMBER))));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ClassInfoActivity.class);
                intent.putExtra(ClassInfoActivity.CLASS_INDEX, i);
                intent.putExtra(ClassInfoActivity.PKG, getArguments().getInt(ARG_SECTION_NUMBER));
                startActivity(intent);
            }
        });
        return rootView;
    }
}