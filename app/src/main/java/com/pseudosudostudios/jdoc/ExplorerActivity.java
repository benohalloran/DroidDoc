package com.pseudosudostudios.jdoc;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.FileInfoFactory;
import data.InfoObject;

import static com.pseudosudostudios.jdoc.R.menu.class_info;

/**
 * The main Activity of the application. Lists all packages in an ExpandableListView which
 * expands to show individual classes.
 */
public class ExplorerActivity extends ExpandableListActivity
        implements ExpandableListView.OnChildClickListener {
    private static final String TAG = "ExplorerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        setListAdapter(new ExplorerAdapter());
        /*
        TODO only one expanded group at a time, scroll to selected group
        getExpandableListView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int prevGroup = -1;

            @Override
            public boolean onGroupClick(ExpandableListView expandableListView,
                                        View view, int group, long l) {
                if (group != prevGroup) {
                    //scroll and collapse
                    if (prevGroup != -1)
                        expandableListView.collapseGroup(prevGroup);
                    prevGroup = group;
                    return false;
                }
                return true;
            }
        });*/
        getExpandableListView().setOnChildClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(class_info, menu);
        return true;
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(this, ClassInfoActivity.class);
        intent.putExtra(ClassInfoActivity.CLASS_INDEX, childPosition);
        intent.putExtra(ClassInfoActivity.PKG, groupPosition);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        return super.onSearchRequested();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return false;
            default:
                return false;
        }
    }

    private class ExplorerAdapter extends BaseExpandableListAdapter {

        private final List<InfoObject>[] sorted;

        public ExplorerAdapter() {
            sorted = new List[getGroupCount()];

        }

        @Override
        public int getGroupCount() {
            return FileInfoFactory.countPackages();
        }

        @Override
        public int getChildrenCount(int i) {
            return FileInfoFactory.countClassesIn(i);
        }

        @Override
        public List<InfoObject> getGroup(int i) {
            return FileInfoFactory.get(i);
        }

        @Override
        public InfoObject getChild(int group, int i2) {
            //TODO sorted
            if (sorted[group] == null) {
                List<InfoObject> temp = clone(getGroup(group));
                Collections.sort(temp);
            }
            return (getGroup(group)).get(i2);
        }

        private List<InfoObject> clone(List<InfoObject> group) {
            ArrayList<InfoObject> list = new ArrayList<InfoObject>(group.size());
            for (InfoObject infoObject : group)
                list.add(infoObject);
            return list;
        }

        @Override
        public long getGroupId(int i) {
            return getGroup(i).hashCode();
        }

        @Override
        public long getChildId(int i, int i2) {
            return getChild(i, i2).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
            View view = getLayoutInflater().
                    inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(FileInfoFactory.getPackage(i));
            return tv;
        }

        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView,
                                 ViewGroup parent) {
            View view = getLayoutInflater().
                    inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView tv = (TextView) view.findViewById(android.R.id.text1);
            tv.setText(getChild(groupPosition, childPosition).getName());
            return tv;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }

    }
}
