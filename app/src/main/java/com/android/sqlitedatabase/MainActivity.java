package com.android.sqlitedatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView imageView, linearLayoutView, gridLayoutView, openPopUpMenu, imageAddNoteBtn;
    TextView textView;
    RecyclerView recyclerView;
    DataBase db;
    ArrayList<String> id;
    ArrayList<String> title;
    ArrayList<String> desc;
    ArrayList<String> date;
    ArrayList<String> time;
    Adapter adapter;
    AlertDialog dialogExit, dialogDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        linearLayoutView = findViewById(R.id.linearLayoutView);
        gridLayoutView = findViewById(R.id.gridLayoutView);
        openPopUpMenu = findViewById(R.id.openPopUpMenu);
        imageAddNoteBtn = findViewById(R.id.imageAddNoteBtn);

        gridLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                linearLayoutView.setVisibility(View.VISIBLE);
                gridLayoutView.setVisibility(View.GONE);
            }
        });

        linearLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                gridLayoutView.setVisibility(View.VISIBLE);
                linearLayoutView.setVisibility(View.GONE);
            }
        });

        imageAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        openPopUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.open_item_men, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.deleteAllNotes:
                                deleteAllConfirmDialog();
                                break;
                            case R.id.about:
                                return true;
                            case R.id.help:
                                return true;
                        }
                        return true;
                    }
                });
            }
        });

        db = new DataBase(MainActivity.this);
        id = new ArrayList<String>();
        title = new ArrayList<>();
        desc = new ArrayList<>();
        date = new ArrayList<>();
        time = new ArrayList<>();

        displayData();

        adapter = new Adapter(MainActivity.this, id, title, desc, date, time);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void deleteAllConfirmDialog() {
        if (dialogDeleteAll == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_all_dialog,
                    (ViewGroup) findViewById(R.id.layoutDeleteAllContainer)
            );
            builder.setView(view);
            dialogDeleteAll = builder.create();
            if (dialogDeleteAll.getWindow() != null) {
                dialogDeleteAll.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBase db = new DataBase(MainActivity.this);
                    db.deleteAllNotes();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDeleteAll.dismiss();
                }
            });

        }
        dialogDeleteAll.show();
    }

    public void displayData() {
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            imageView.setImageResource(R.drawable.nodata);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                id.add(cursor.getString(0));
                title.add(cursor.getString(1));
                desc.add(cursor.getString(2));
                date.add(cursor.getString(3));
                time.add(cursor.getString(4));
//                datetime.add(cursor.getString(5));
            }
        }
        cursor.close();
//                byte[] image = cursor.getBlob(5);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//                imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBackPressed() {
        showExitConfirmDialog();
    }

    private void showExitConfirmDialog() {
        if(dialogExit == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_exit_dialog,
                    (ViewGroup) findViewById(R.id.layoutExitContainer)
            );
            builder.setView(view);
            builder.setCancelable(false);
            dialogExit = builder.create();
            if(dialogExit.getWindow() != null){
                dialogExit.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textExit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogExit.dismiss();
                }
            });

        }
        dialogExit.show();
    }
}