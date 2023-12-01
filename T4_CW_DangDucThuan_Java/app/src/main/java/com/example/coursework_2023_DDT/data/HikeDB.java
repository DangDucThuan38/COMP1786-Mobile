package com.example.coursework_2023_DDT.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class HikeDB {
    SQLiteDatabase db;
    public MutableLiveData<HikeEntity> hike = new MutableLiveData<>();
    public  MutableLiveData<List<HikeEntity>> hikeList = new MutableLiveData<List<HikeEntity>>();

    public HikeDB(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    @SuppressLint("Range")
    public List<HikeEntity> get(String DATABASE_CREATE, String ... selectArgs){
        List<HikeEntity> list = new ArrayList<>();
        Cursor cursorss = db.rawQuery(DATABASE_CREATE, selectArgs);

        while (cursorss.moveToNext()){
            HikeEntity hikeDB = new HikeEntity();
            hikeDB.setId(cursorss.getString(cursorss.getColumnIndex("hike_id")));
            hikeDB.setName(cursorss.getString(cursorss.getColumnIndex("name")));
            hikeDB.setDestination(cursorss.getString(cursorss.getColumnIndex("destination")));
            hikeDB.setDescription(cursorss.getString(cursorss.getColumnIndex("description")));
            hikeDB.setDate(cursorss.getString(cursorss.getColumnIndex("date_hike")));
            hikeDB.setParticipant(cursorss.getInt(cursorss.getColumnIndex("participant")));
            hikeDB.setParking(cursorss.getString(cursorss.getColumnIndex("parking")));
            hikeDB.setLevel(cursorss.getString(cursorss.getColumnIndex("level")));
            hikeDB.setLength(cursorss.getString(cursorss.getColumnIndex("length")));
            hikeDB.setLocation(cursorss.getString(cursorss.getColumnIndex("location")));
            list.add(hikeDB);
        }
        return list;
    }

    public List<HikeEntity> getAll(){
        String dbSelect = "SELECT * FROM hikes";
        return get(dbSelect);
    }


    public HikeEntity getByID(String id){
        String dbGetOne = "SELECT * FROM hikes WHERE hike_id = ?";
        List<HikeEntity> list = get(dbGetOne, id);
        return list.get(0);
    }

    public long insert(HikeEntity hikeDB){
        ContentValues contentValuesHike = new ContentValues();
        contentValuesHike.put("name", hikeDB.getName());
        contentValuesHike.put("destination", hikeDB.getDestination());
        contentValuesHike.put("date_hike", hikeDB.getDate().toString());
        contentValuesHike.put("participant", hikeDB.getParticipant());
        contentValuesHike.put("description", hikeDB.getDescription());
        contentValuesHike.put("parking", hikeDB.getParking());
        contentValuesHike.put("level", hikeDB.getLevel());
        contentValuesHike.put("length", hikeDB.getLength());
        contentValuesHike.put("location", hikeDB.getLocation());

        return db.insert( "hikes",null,contentValuesHike);
    }

    public long update(HikeEntity hikeDB){
        ContentValues contentValuesHike = new ContentValues();
        contentValuesHike.put("name", hikeDB.getName());
        contentValuesHike.put("destination", hikeDB.getDestination());
        contentValuesHike.put("date_hike", hikeDB.getDate().toString());
        contentValuesHike.put("participant", hikeDB.getParticipant());
        contentValuesHike.put("description", hikeDB.getDescription());
        contentValuesHike.put("parking", hikeDB.getParking());
        contentValuesHike.put("level", hikeDB.getLevel());
        contentValuesHike.put("length", hikeDB.getLength());
        contentValuesHike.put("location", hikeDB.getLocation());
        return db.update( "hikes",contentValuesHike, "hike_id=?", new String[]{String.valueOf(hikeDB.getId())});
    }

    public int delete(String id){
        return db.delete("hikes", "hike_id=?", new String[]{String.valueOf(id)});
    }
    public void deleteAll() {
        db.execSQL("DELETE FROM hikes");
    }

    public void search(String search){
        String dbSearch = "SELECT * FROM hikes WHERE name LIKE ('%" + search + "%')" +
                " OR " + "date_hike LIKE ('%" + search + "%')" +
                " OR " + "destination LIKE ('%" + search + "%')";
        List<HikeEntity> list = get(dbSearch);
        hikeList.setValue(list);
    }
}
