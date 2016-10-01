package com.codepath.simpletodo;

/**
 * Created by aditikakadebansal on 9/27/16.
 */
public interface ISimpleTodoEntity {

    public long getPrimaryKey();
    public String getDescription();
    public String getTableName();
}
