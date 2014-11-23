package com.pseudosudostudios.jdoc;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import data.FileInfoFactory;


public class SearchActivity extends ListActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "Search Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        ListAdapter adapter = null;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //actually search
            Log.i(TAG, query);
            adapter =
                    new SimpleAdapter(this,
                            FileInfoFactory.getSearchResults(query),
                            android.R.layout.simple_list_item_1,
                            new String[]{FileInfoFactory.SEARCH_KEY},
                            new int[]{android.R.id.text1});
        }
        getListView().setOnItemClickListener(this);
        if (adapter != null)
            setListAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        assert view instanceof TextView;
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            String value = tv.getText().toString();
            Intent intent = new Intent(this, ClassInfoActivity.class);
            intent.putExtra(ClassInfoFrag.FULL_PACKAGE, value);
            startActivity(intent);
        } else
            Log.i(TAG, "Unknown view: " + view.toString());


    }
}
