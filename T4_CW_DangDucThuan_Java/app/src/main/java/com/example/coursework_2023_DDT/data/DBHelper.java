package com.example.coursework_2023_DDT.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "company";
    private static final String TABLE_NAME = "hikes";
    private static final String DROP = "DROP TABLE IF EXISTS ";
    private static final String ID_HIKE = "hike_id";
    private static final String NAME = "name";
    private static final String DESTINATION = "destination";
    private static final String PARTICIPANT = "participant";
    private static final String DESCRIPTION = "description";
    private static final String PARKING = "parking";
    private static final String LEVEL = "level";
    private static final String LENGTH = "length";
    private static final String LOCATION = "location";
    private static final String OBSERVATION_TABLE = "observation";
    private static final String DATE = "date_hike";

    private static final String DATABASE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s INTEGER, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)"
            , TABLE_NAME, ID_HIKE, NAME, DESTINATION, DESCRIPTION, DATE, PARKING, PARTICIPANT,
            LEVEL, LENGTH, LOCATION);
    private static final String OBSERVATION_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s INTEGER," +
                    "FOREIGN KEY(%s) REFERENCES %s(%s))",
            OBSERVATION_TABLE, "observation_id", "observation_name", "observation_date",
            "observation_comment", "hike_id", "hike_id", TABLE_NAME, ID_HIKE);

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(OBSERVATION_CREATE);
        } catch (Exception e) {
            Log.e("DBHelper", "Error creating tables", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL(DROP + TABLE_NAME);
        DB.execSQL(DROP + OBSERVATION_TABLE);
        onCreate(DB);
        Log.w(this.getClass().getName(), DATABASE_NAME + " DATABASE UPGRADE TO VERSION " + newVersion + "- OLD DATA LOST");
    }
}
