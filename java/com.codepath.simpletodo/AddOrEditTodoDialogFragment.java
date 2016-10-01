package com.codepath.simpletodo;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.codepath.simpletodo.Model.Todo;

import java.text.ParseException;
import java.util.Locale;


/**
 * Created by aditikakadebansal on 9/27/16.
 */
public class AddOrEditTodoDialogFragment extends DialogFragment implements IDialogFragmentSupportsEntityEdit {

    EditText todoTextField;
    PostsDatabaseHelper databaseHelper;
    DatePicker todoDatePicker;
    TimePicker todoTimePicker;
    Todo todoToUpdate;


    private TodosTabActivity getTodosTabActivity() {
        return (TodosTabActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_todo_dialog, container, false);
        /*Setting style for dialog fragment*/

        setStyle(DialogFragment.STYLE_NORMAL, R.style.addOrEditDialogFragmentTheme);
        getDialog().setTitle(CommonConstants.appName);
        /*getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
*/
        /*End of style for dialog fragment*/
        ImageButton saveTodoButton = (ImageButton)rootView.findViewById(R.id.saveTodoButton);
        ImageButton deleteTodoButton = (ImageButton)rootView.findViewById(R.id.deleteTodoButton);
        todoTextField = (EditText)rootView.findViewById(R.id.newTodoEditText);
        todoDatePicker = (DatePicker) rootView.findViewById(R.id.todoDatePicker);
        todoDatePicker.setMinDate(System.currentTimeMillis() - 1000);
        todoTimePicker = (TimePicker) rootView.findViewById(R.id.todoTimePicker);

        if (todoToUpdate != null) {
            setHasOptionsMenu(true);
            todoTextField.setText(todoToUpdate.getDescription());
            try {
                todoDatePicker.updateDate(todoToUpdate.getYear(), todoToUpdate.getMonth(), todoToUpdate.getDayOfMonth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            deleteTodoButton.setVisibility(View.INVISIBLE);
            deleteTodoButton.setEnabled(false);
        }


        databaseHelper = PostsDatabaseHelper.getInstance(rootView.getContext());
        saveTodoButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodosTabActivity activity = getTodosTabActivity();
                Todo todo = null; String toastErrorMessage="";
                try {
                if (todoToUpdate != null) {
                    if(todoTextField.getText().toString().isEmpty()) {
                        todo = null;
                        toastErrorMessage = CommonConstants.todoValidationMessage;
                    }
                    else {
                        todo = databaseHelper.updateSingleTodo(
                                todoToUpdate,
                                todoTextField.getText().toString(),
                                getDateTimeFromPickers()
                        );
                        toastErrorMessage = CommonConstants.errorMessage;
                    }
                    if(validateTodo(todo, toastErrorMessage)) {
                        setAlarm(todo);
                        if (!todo.isDeadlineToday(TodosTabActivity.date)) {
                            activity.adapter.remove(todo);
                        }
                        activity.adapter.notifyDataSetChanged();
                    }

                    } else {
                          if(todoTextField.getText().toString().isEmpty()) {
                                todo = null;
                                toastErrorMessage = CommonConstants.todoValidationMessage;
                            }else {
                                todo = databaseHelper.addTodo(
                                  todoTextField.getText().toString(),
                                  CommonDateFunctions.convertDateToMMDDYYYY(new Date()),
                                  getDateTimeFromPickers());
                                toastErrorMessage = CommonConstants.errorMessage;
                            }
                        if(validateTodo(todo,toastErrorMessage)) {
                            setAlarm(todo);
                            if (todo.isDeadlineToday(TodosTabActivity.date)) {
                                activity.adapter.add(todo);
                            }
                        }
                    }

                }catch(ParseException parseException){
                    parseException.printStackTrace();
                }
                dismiss();
            }
        });

        deleteTodoButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodosTabActivity activity = getTodosTabActivity();
                if (todoToUpdate != null){
                    if(databaseHelper.delete(todoToUpdate)){
                        activity.adapter.remove(todoToUpdate);
                        activity.adapter.notifyDataSetChanged();
                    }
                }
                dismiss();
            }
        });




        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onResume()
    {
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

            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            windowParams.dimAmount =1;
            window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
            window.setAttributes(windowParams);

            }
    }

    private void setAlarm(Todo todo) throws ParseException{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.yyyyMMddHHmmss);
        cal.setTime(sdf.parse(todo.getDeadline()));
        invokeAlarm(getTodosTabActivity().getApplicationContext(), cal, todo);
    }
    void invokeAlarm(Context context, Calendar calendar, Todo todo)
    {
        AlarmManager alarm = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        final String SOME_ACTION = "com.codepath.simpletodo.AlarmReceiver";
        IntentFilter intentFilter = new IntentFilter(SOME_ACTION);
        AlarmReceiver mReceiver = (new AlarmReceiver()).setTodo(todo);
        context.registerReceiver(mReceiver, intentFilter);
        Intent i2 = new Intent(SOME_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i2, 0);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        Toast.makeText(context, CommonConstants.todoSaved, Toast.LENGTH_LONG).show();
    }


    private boolean validateTodo(Todo todo,String toastErrorMessage) {
        if (todo == null) {
            Context context = getTodosTabActivity().getApplicationContext();
            Toast.makeText(context, toastErrorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getDateTimeFromPickers() throws ParseException {

            String dateTime = Integer.toString(todoDatePicker.getYear()) +
                    "-" + Integer.toString(todoDatePicker.getMonth() + 1) +
                    "-" + Integer.toString(todoDatePicker.getDayOfMonth()) +
                    " " + Integer.toString(todoTimePicker.getCurrentHour()) +
                    ":" + Integer.toString(todoTimePicker.getCurrentMinute()) +
                    ":00";


            return CommonDateFunctions.getDateTimeInYYMMDDHHHMMSS(dateTime);
        }


    @Override
    public void setEntityToUpdate(ISimpleTodoEntity entityToUpdate) {
        todoToUpdate = (Todo)entityToUpdate;
    }
}
