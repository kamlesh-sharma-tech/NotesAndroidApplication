package com.android.sqlitedatabase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    TextView updateNoteId;
    EditText title_id,desc_id;
    ImageView imageBack,updateNote;
    String id,currentDate,currentTime;
    DateFormat getDate,getTime;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_id = findViewById(R.id.title_id);
        desc_id = findViewById(R.id.desc_id);
        imageBack = findViewById(R.id.imageBack);
        updateNote = findViewById(R.id.updateNote);
        updateNoteId = findViewById(R.id.updateNoteId);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        updateNoteId.setText(String.format("Update Note - %s", id));
        title_id.setText(intent.getStringExtra("title"));
        desc_id.setText(intent.getStringExtra("desc"));

        //show the current date
        getDate = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = getDate.format(new Date());

        //show the current time
        getTime = new SimpleDateFormat("hh:mm aa");
        currentTime = getTime.format(new Date());

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotes();
            }
        });

    }


    public void updateNotes() {
        DataBase db = new DataBase(UpdateActivity.this);
        db.updateNote(title_id.getText().toString(),desc_id.getText().toString(),id);

        Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("title",title_id.getText().toString());
        intent.putExtra("desc",desc_id.getText().toString());
        intent.putExtra("date",currentDate);
        intent.putExtra("time",currentTime);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}