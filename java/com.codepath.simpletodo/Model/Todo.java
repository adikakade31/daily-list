package com.codepath.simpletodo.Model;

import android.database.Cursor;

import com.codepath.simpletodo.ISimpleTodoEntity;
import com.codepath.simpletodo.PostsDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

/**
 * Created by aditikakadebansal on 9/23/16.
 */
public class Todo
        implements ISimpleTodoEntity {

    private String description;
    private long primaryKey;
    private String date;
    private String deadline;
    private String priority;


    public Todo(String description, long primaryKey, String date, String deadline){
        this.description = description;
        this.primaryKey = primaryKey;
        this.date = date;
        this.deadline = deadline;
    }

    public int getYear() throws ParseException {
        return getFormattedDeadline().getYear()+1900;
    }

    public int getMonth() throws ParseException {
        return getFormattedDeadline().getMonth();
    }

    public boolean isDeadlineToday(String datePickerDate) throws ParseException {

        Date date = (new SimpleDateFormat("yyyy-MM-dd")).parse(getDeadline());
        Date today = (new SimpleDateFormat("MM-dd-yyyy")).parse(datePickerDate);
        return today.getDate() == date.getDate() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear();
    }


    public int getDayOfMonth() throws ParseException {
        return getFormattedDeadline().getDate();
    }

    private Date getFormattedDeadline() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(this.deadline);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrimaryKey() {
        return primaryKey;
    }

    public String toString(){
        return description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public static Todo getInstance(Cursor cursor) {
        return new Todo(cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.DESCRIPTION)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.PK))),
                cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.DATE)),
                cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.DEADLINE)));
    }



    @Override
    public String getTableName() {
        return PostsDatabaseHelper.TABLE_TODO;
    }

}
