package com.codepath.simpletodo;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aditikakadebansal on 9/27/16.
 */
abstract class SimpleTodoTabActivity extends Activity {
    ListView listViewer;
    ArrayList<ISimpleTodoEntity> entities;
    ArrayAdapter <ISimpleTodoEntity> adapter;
    PostsDatabaseHelper databaseHelper;
    ImageButton addButton;
    static String date;
    private final int REQUEST_CODE = 20;

    abstract protected int getResourceID();
    abstract protected int getContentViewID();
    abstract protected String getTabType();
    abstract protected Context getActivityContext();
    abstract protected int getAddButtonID();
    abstract protected DialogFragment getAddDialogFragment();
    abstract protected void setTabActivity();


    public void renderTab(String selectedDate){
        setContentView(getContentViewID());
        this.date = selectedDate;
        databaseHelper = PostsDatabaseHelper.getInstance(getActivityContext());
        addButton = (ImageButton)findViewById(getAddButtonID());
        listViewer = (ListView) findViewById(getResourceID());
        entities = new ArrayList<ISimpleTodoEntity>();
        adapter = new ArrayAdapter<ISimpleTodoEntity>(this, android.R.layout.simple_list_item_1, entities){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                //Changing default text size in ListView.
                textView.setTextSize(12);
                textView.setMinHeight(30);
                textView.setMinimumHeight(30);
                return view;
            }
        };

        if (date == null) {
            // Something wrong happened.
        }
        readEntitiesFromDBForAdapter(date);
        listViewer.setAdapter(adapter);
        listViewer.setEmptyView(findViewById(R.id.empty));
        listViewer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                ISimpleTodoEntity entity = entities.remove(position);
                adapter.notifyDataSetChanged();
                databaseHelper.delete(entity);
                return true;
            }
        });

        listViewer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment addDialogFragment = getAddDialogFragment();
                if (addDialogFragment instanceof IDialogFragmentSupportsEntityEdit) {
                    ((IDialogFragmentSupportsEntityEdit) addDialogFragment).setEntityToUpdate(entities.get(position));
                }
                addDialogFragment.show(fragmentManager, "Sample Fragment");
            }
        });

        addButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment addDialogFragment = getAddDialogFragment();
                Bundle args = new Bundle();
                args.putString(CommonConstants.date, date);
                addDialogFragment.setArguments(args);
                addDialogFragment.show(fragmentManager, "Sample Fragment");
            }
        });

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTabActivity();

        if(date == null)
            date = getIntent().getStringExtra(CommonConstants.date);
        renderTab(date);
    }


    public void readEntitiesFromDBForAdapter(String date) {

        List<ISimpleTodoEntity> entitiesFromDB = databaseHelper.getAllEntities(date, this.getTabType());
        entities.clear();
        for (ISimpleTodoEntity entity : entitiesFromDB) {
            entities.add(entity);
        }
        adapter.notifyDataSetChanged();
    }

}
