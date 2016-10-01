package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText editNoteDescription;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra(CommonConstants.itemText);
        position = getIntent().getIntExtra(CommonConstants.position,0);
        editNoteDescription = (EditText) findViewById(R.id.editItemText);
        editNoteDescription.setText(itemText);

    }
    public void onSaveItem(View view){
        Intent data = new Intent();
        data.putExtra(CommonConstants.itemText, editNoteDescription.getText().toString());
        data.putExtra(CommonConstants.position, position);
        setResult(RESULT_OK, data);
        finish();
    }
}
