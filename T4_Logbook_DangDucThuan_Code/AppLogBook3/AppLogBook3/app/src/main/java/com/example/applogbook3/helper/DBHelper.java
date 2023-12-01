package com.example.applogbook3.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2; // Thay đổi phiên bản cơ sở dữ liệu
    static final  String DATABASE_NAME = "logbook3";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY autoincrement, " +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "image BLOB)"; // Thêm cột image kiểu BLOB cho ảnh
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM users";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("name", cursor.getString(1));
                map.put("email", cursor.getString(2));
                map.put("date", cursor.getString(3));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void insert(String name, String email, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("date", date);
        values.put("image", image);
        db.insert("users", null, values);
    }

    public void update(int id, String name, String email, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("date", date);
        db.update("users", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "id = ?", new String[]{String.valueOf(id)});
    }


    @SuppressLint("Range")
    public byte[] getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { "image" };
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        byte[] imageData = null;

        if (cursor.moveToFirst()) {
            imageData = cursor.getBlob(cursor.getColumnIndex("image"));
        }

        cursor.close();
        return imageData;
    }

}
