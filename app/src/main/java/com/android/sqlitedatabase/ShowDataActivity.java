package com.android.sqlitedatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

public class ShowDataActivity extends AppCompatActivity {
    TextView idD,dataTitle,dataDescription;
    ImageView imageBack,imageButton;
    ScrollView layout;
    String getNoteDate,getNoteTime;
    BottomSheetDialog bottomSheetDialog;
    AlertDialog dialogDelete;

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        idD = findViewById(R.id.idD);
        dataTitle = findViewById(R.id.dataTitle);
        dataDescription = findViewById(R.id.dataDescription);
        imageBack = findViewById(R.id.imageBack);
        layout = findViewById(R.id.layout);

        Intent intent = getIntent();
        getNoteDate = intent.getStringExtra("date");
        getNoteTime = intent.getStringExtra("time");
//        actionBar.setTitle("Note No. - "+intent.getStringExtra("id"));
        idD.setText(intent.getStringExtra("id"));
        dataTitle.setText(intent.getStringExtra("title"));
        dataDescription.setText(intent.getStringExtra("desc"));

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageButton = findViewById(R.id.open_bottom_sheet_dialog);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to open bottom modal sheet
                bottomSheetDialog = new BottomSheetDialog(ShowDataActivity.this,R.style.BottomSheetStyle);
                View sheetView = LayoutInflater.from(ShowDataActivity.this).inflate(R.layout.bottom_sheet_dialog,
                        (LinearLayout)findViewById(R.id.bottomSheetBox));
                TextView noteTitleSheetText = sheetView.findViewById(R.id.noteTitle);
                noteTitleSheetText.setText(dataTitle.getText().toString());

                sheetView.findViewById(R.id.detail_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                                String numberID = idD.getText().toString();
                                detailDialog(numberID,getNoteDate,getNoteTime);
                                bottomSheetDialog.dismiss();
                    }
                });

                sheetView.findViewById(R.id.edit_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(ShowDataActivity.this,UpdateActivity.class);
                        editIntent.putExtra("id",getIntent().getStringExtra("id"));
                        editIntent.putExtra("title",dataTitle.getText().toString());
                        editIntent.putExtra("desc",dataDescription.getText().toString());
                        startActivity(editIntent);
                        bottomSheetDialog.dismiss();
                    }
                });

                sheetView.findViewById(R.id.delete_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String position = getIntent().getStringExtra("id");
                        deleteNoteConfirmDialog(position);
                        bottomSheetDialog.dismiss();
                    }
                });

                sheetView.findViewById(R.id.share_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent =   new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        String title = dataTitle.getText().toString();
                        String desc = dataDescription.getText().toString();
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT,title);
                        shareIntent.putExtra(Intent.EXTRA_TEXT,desc);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                        bottomSheetDialog.dismiss();
                    }
                });

                    sheetView.findViewById(R.id.copy_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("TextView",dataDescription.getText().toString());
                            clipboard.setPrimaryClip(clip);
                            Snackbar snackbar = Snackbar.make(layout, "Message Copied", Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                            bottomSheetDialog.dismiss();
                        }
                    });

                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    private void deleteNoteConfirmDialog(String position) {


        if(dialogDelete == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowDataActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_dialog,
                    (ViewGroup) findViewById(R.id.layoutDeleteContainer)
            );
            builder.setView(view);
            dialogDelete = builder.create();
            if(dialogDelete.getWindow() != null){
                dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            view.findViewById(R.id.textDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBase db = new DataBase(ShowDataActivity.this);
                    db.deleteOneNote(position);
                    Intent deleteIntent = new Intent(ShowDataActivity.this,MainActivity.class);
                    deleteIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(deleteIntent);
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete.dismiss();
                }
            });

        }
        dialogDelete.show();
    }

    private void detailDialog(String numberID, String noteDate, String noteTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDataActivity.this);
        builder.setTitle("About Note!");
        builder.setMessage("Note Number : "+numberID+"\nReadable : Yes"+"\nEditable : Yes"+"\nDate Created : "+noteDate+"\nTime Created : "+noteTime);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_detail_icon);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        builder.show();
    }

}