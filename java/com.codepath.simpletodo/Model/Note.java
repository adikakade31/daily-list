package com.codepath.simpletodo.Model;

import android.database.Cursor;

import com.codepath.simpletodo.IDBOperations;
import com.codepath.simpletodo.ISimpleTodoEntity;
import com.codepath.simpletodo.PostsDatabaseHelper;

import java.util.List;

/**
 * Created by aditikakadebansal on 9/23/16.
 */
public class Note
        implements IDBOperations, ISimpleTodoEntity {

    private String description;
    private long primaryKey;
    private String date;

    public Note(String description, long primaryKey, String date){
        this.description = description;
        this.primaryKey = primaryKey;
        this.date = date;
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

    public static Note getInstance(Cursor cursor) {
        return new Note(cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.DESCRIPTION)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.PK))),
                cursor.getString(cursor.getColumnIndex(PostsDatabaseHelper.DATE)));
    }

    @Override
    public String getTableName() {
        return PostsDatabaseHelper.TABLE_NOTE;
    }

    public List<String> getAddStatements() {
        return null;
    }
    public List<String> getDeleteStatements() {
        return null;
    }
}
