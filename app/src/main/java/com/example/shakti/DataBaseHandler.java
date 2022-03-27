package com.example.shakti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shakti.db";
    public static final String TABLE_NAME = "contact";
    public static final String COL1 = "ID";
    public static final String COL2 = "phone";

    public DataBaseHandler(Context context) { super(context,DATABASE_NAME,null,1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table "+TABLE_NAME+ " ("+ COL1 +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }

    public boolean insertPhoneNumber(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, phone);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1;
    }

    public boolean deletePhoneNumber(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, phone);
        long result = db.delete(TABLE_NAME, "Phone=?",new String[]{phone});
        db.close();
        return result != -1;
    }

    public ArrayList<String> phoneNumberList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> numbers = new ArrayList<>();
        Cursor data = db.rawQuery("SELECT "+ COL2 +" FROM "+TABLE_NAME,null);
        while(data.moveToNext()) {
            String phoneNumber = data.getString(0);
            numbers.add(phoneNumber);
        }
        return numbers;
    }
}
