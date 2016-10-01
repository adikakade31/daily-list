package com.codepath.simpletodo;

import android.content.Context;

/**
 * Created by aditikakadebansal on 9/27/16.
 */
public class TodosTabActivity extends SimpleTodoTabActivity {

    static TodosTabActivity todosTabActivity;
    protected int getResourceID() {
        return R.id.todosListView;
    }


    protected int getContentViewID() {
        return R.layout.activity_todo;
    }

    protected String getTabType() {
        return "todos";
    }

    protected Context getActivityContext() {
        return TodosTabActivity.this;
    }
    protected int getAddButtonID(){
        return R.id.addTodosButton;
    }
    protected AddOrEditTodoDialogFragment getAddDialogFragment() {
        return new AddOrEditTodoDialogFragment();
    }
    protected void setTabActivity(){
        todosTabActivity = this;
    }
    public static void reRender(String selectedDate){
        date = selectedDate;
        if(todosTabActivity == null)
            return;
        todosTabActivity.renderTab(date);
    }

}
