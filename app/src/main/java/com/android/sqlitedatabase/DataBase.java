package com.android.sqlitedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;
import androidx.annotation.Nullable;

import io.github.muddz.styleabletoast.StyleableToast;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notedb.db";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_TABLE = "notetable";

    //Column names for table
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_IMAGE = "image";
    private Context context;

    public DataBase(@Nullable Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //command to create the table
        String query = "CREATE TABLE "+DATABASE_TABLE+" ("+COLUMN_ID+"INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_TITLE + " TEXT, "+COLUMN_DESCRIPTION + " TEXT, "+COLUMN_DATE + " TEXT, "+
                COLUMN_TIME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }

    //Function to insert data into the database
    public void addNote(String title, String description, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DESCRIPTION,description);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_TIME,time);
        long result = db.insert(DATABASE_TABLE,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed to Add Note", Toast.LENGTH_SHORT).show();
        }else{
            new StyleableToast
                    .Builder(context)
                    .text("Note Added Successfully!")
                    .textColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .textSize(18)
                    .cornerRadius(8)
                    .length(25)
                    .show();
        }
    }

    //Function to read all rows from the database
    Cursor readAllData(){
        String query = "SELECT * FROM " + DATABASE_TABLE+" ORDER BY "+COLUMN_ID+" DESC";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = null;
        if (database!=null){
            cursor=database.rawQuery(query,null);
        }
        return cursor;
    }

    //Function to delete all the rows from the database
    void deleteAllNotes(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + DATABASE_TABLE;
        database.execSQL(query);
    }

    //Function to delete single row in the database
    void deleteOneNote(String row_id){
        SQLiteDatabase database = this.getWritableDatabase();
        long result = database.delete(DATABASE_TABLE,"id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else{
            new StyleableToast
                    .Builder(context)
                    .text("Note Deleted Successfully!")
                    .textColor(Color.WHITE)
                    .backgroundColor(Color.RED)
                    .textSize(18)
                    .cornerRadius(8)
                    .length(25)
                    .show();
        }
    }

    //Function to update the data from database
    void updateNote(String title,String description,String id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DESCRIPTION,description);
        long resultValue = db.update(DATABASE_TABLE,cv,"id=?",new String[]{id});
        if (resultValue == -1){
            Toast.makeText(context, "Not Updated", Toast.LENGTH_SHORT).show();
        }else{
            new StyleableToast
             .Builder(context)
                    .text("Note Updated Successfully!")
                    .textColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .textSize(18)
                    .cornerRadius(8)
                    .length(25)
                    .show();
        }
    }

}
