package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText editItemText;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra("itemText");
        position = getIntent().getIntExtra("position",0);
        editItemText = (EditText) findViewById(R.id.editItemText);
        editItemText.setText(itemText);

    }
    public void onSaveItem(View view){
        Intent data = new Intent();
        data.putExtra("itemText", editItemText.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        finish();
    }
}
