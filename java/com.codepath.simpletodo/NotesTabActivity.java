package com.codepath.simpletodo;

import android.content.Context;

/**
 * Created by aditikakadebansal on 9/27/16.
 */
public class NotesTabActivity extends SimpleTodoTabActivity {

    static NotesTabActivity notesTabActivity;
    protected int getResourceID() {
        return R.id.notesListView;
    }

    protected int getContentViewID() {
        return R.layout.activity_personal_note;
    }

    protected String getTabType() {
        return "notes";
    }

    protected Context getActivityContext() {
        return NotesTabActivity.this;
    }
    protected int getAddButtonID(){
        return R.id.addNoteButton;
    }
    protected AddorEditNoteDialogFragment getAddDialogFragment() {
        return new AddorEditNoteDialogFragment();
    }
    protected void setTabActivity(){
        notesTabActivity = this;
    }
    public static void reRender(String selectedDate){
        date = selectedDate;
        if(notesTabActivity == null)
            return;
        notesTabActivity.renderTab(date);
    }

}
