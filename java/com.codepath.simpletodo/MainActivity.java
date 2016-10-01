package com.codepath.simpletodo;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TabHost;
import java.util.Date;
import java.util.Calendar;
import android.widget.TabHost.OnTabChangeListener;

import android.widget.DatePicker.OnDateChangedListener;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

    DatePicker datePicker;
    TabHost tabHostWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        OnDateChangedListener dateChangeListener = new OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                NotesTabActivity.reRender(getDate(year, monthOfYear,dayOfMonth));
                TodosTabActivity.reRender(getDate(year, monthOfYear,dayOfMonth));
            }
        };



        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), dateChangeListener);

        tabHostWindow = (TabHost)findViewById(R.id.tabHost);
        tabHostWindow.setup(this.getLocalActivityManager());
        //Tab for displaying to-do items
        TabHost.TabSpec todoTab = tabHostWindow.newTabSpec(CommonConstants.ToDoTabNAme);
        Intent todoTabIntent = new Intent(this,TodosTabActivity.class);
        todoTabIntent.putExtra(CommonConstants.date, getDate(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth()));
        //todoTabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        todoTab.setContent(todoTabIntent);
        todoTab.setIndicator(CommonConstants.ToDoTabNAme);
        tabHostWindow.addTab(todoTab);
        //Tab for displaying notes
        TabHost.TabSpec notesTab = tabHostWindow.newTabSpec(CommonConstants.NotesTabNAme);
        Intent notesTabIntent = new Intent(this,NotesTabActivity.class);
        notesTabIntent.putExtra(CommonConstants.date, getDate(datePicker.getYear(), datePicker.getMonth(),datePicker.getDayOfMonth()));
        //notesTabIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notesTab.setContent(notesTabIntent);
        notesTab.setIndicator(CommonConstants.NotesTabNAme);
        tabHostWindow.addTab(notesTab);
    }

    /*
    Get selected date in String format
     */
    public String getDate(int year, int month, int day){
        Date datePickerDate = new Date(year - 1900, month, day);
        return CommonDateFunctions.convertDateToMMDDYYYY(datePickerDate);
    }
}
