package com.codepath.simpletodo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.simpletodo.Model.Note;

import java.text.ParseException;


/**
 * Created by aditikakadebansal on 9/27/16.
 */
public class AddorEditNoteDialogFragment extends DialogFragment
    implements IDialogFragmentSupportsEntityEdit {

    EditText noteTextField;
    PostsDatabaseHelper databaseHelper;
    Note noteToUpdate;

    private NotesTabActivity getNotesTabActivity() {
        return (NotesTabActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_note_dialog, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.addOrEditDialogFragmentTheme);
        getDialog().setTitle(CommonConstants.appName);

        ImageButton saveNoteButton = (ImageButton)rootView.findViewById(R.id.saveNoteButton);
        ImageButton deleteNoteButton = (ImageButton)rootView.findViewById(R.id.deleteNoteButton);
        noteTextField = (EditText)rootView.findViewById(R.id.newNoteEditText);
        databaseHelper = PostsDatabaseHelper.getInstance(rootView.getContext());
        if (noteToUpdate != null) {
            setHasOptionsMenu(true);
            noteTextField.setText(noteToUpdate.getDescription());
        }
       else{
            deleteNoteButton.setVisibility(View.INVISIBLE);
            deleteNoteButton.setEnabled(false);
        }

        saveNoteButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesTabActivity activity = getNotesTabActivity();
                Note note = null;
                if (noteToUpdate != null) {
                  note = databaseHelper.updateSingleNote(
                          noteToUpdate,
                          noteTextField.getText().toString()
                  );
                    validateNote(note);
                    activity.adapter.notifyDataSetChanged();
                } else {
                    note = databaseHelper.addNote(
                            noteTextField.getText().toString(),
                            getArguments().getString(CommonConstants.date)
                    );
                    validateNote(note);
                    activity.adapter.add(note);
                }


                dismiss();
            }
        });
        deleteNoteButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesTabActivity activity = getNotesTabActivity();
                if (noteToUpdate != null){
                    if(databaseHelper.delete(noteToUpdate)){
                        activity.adapter.remove(noteToUpdate);
                        activity.adapter.notifyDataSetChanged();
                    }
                }
                dismiss();
            }
        });
        return rootView;
    }

    @Override
    public void onResume(){
       super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        window.setGravity(Gravity.CENTER);

    }
    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        if (dialog != null) {
            windowParams.dimAmount =1;
            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
            window.setAttributes(windowParams);

        }
    }

    private void validateNote(Note note) {
        if (note == null) {
            Context context = getNotesTabActivity().getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, CommonConstants.errorMessage, duration);
            toast.show();
            return;
        }
    }

    @Override
    public void setEntityToUpdate(ISimpleTodoEntity entityToUpdate) {
        this.noteToUpdate = (Note)entityToUpdate;
    }
}
