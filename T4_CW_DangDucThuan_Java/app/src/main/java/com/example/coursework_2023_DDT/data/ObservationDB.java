package com.example.coursework_2023_DDT.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class ObservationDB {
    SQLiteDatabase db;
    String hikeID;
    public MutableLiveData<ObservationEntity> observation = new MutableLiveData<>();
    public  MutableLiveData<List<ObservationEntity>> observationList = new MutableLiveData<List<ObservationEntity>>();

    public ObservationDB(Context context, String hikeID) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        this.hikeID = hikeID;
    }
    @SuppressLint("Range")
    public List<ObservationEntity> get(String OBSERVATION_CREATE, String ... selectArgs){
        List<ObservationEntity> list = new ArrayList<>();
        Cursor currsors = db.rawQuery(OBSERVATION_CREATE, selectArgs);

        while (currsors.moveToNext()){
            ObservationEntity observationDB = new ObservationEntity();
            observationDB.setO_ID(currsors.getString(currsors.getColumnIndex("observation_id")));
            observationDB.setType(currsors.getString(currsors.getColumnIndex("observation_name")));
            observationDB.setDate(currsors.getString(currsors.getColumnIndex("observation_date")));
            observationDB.setNotes(currsors.getString(currsors.getColumnIndex("observation_comment")));
            observationDB.setH_ID(currsors.getString(currsors.getColumnIndex("hike_id")));
            list.add(observationDB);
        }
        return list;
    }
    public List<ObservationEntity> getAll(){
        String dbSelect = "SELECT * FROM observation WHERE hike_id =?";
        return get(dbSelect, hikeID);
    }
    public ObservationEntity getByID(String id){
        String dbGetOne = "SELECT * FROM observation WHERE hike_id = ? and observation_id =?";

        List<ObservationEntity> list = get(dbGetOne,hikeID, id);
        observation.setValue(list.get(0));
        return list.get(0);
    }


    public long insert(ObservationEntity observationDB){
        ContentValues contentValuesobservation = new ContentValues();
        contentValuesobservation.put("observation_name", observationDB.getType());
        contentValuesobservation.put("observation_date", observationDB.getDate());
        contentValuesobservation.put("observation_comment", observationDB.getNotes());
        contentValuesobservation.put("hike_id", observationDB.getH_ID());

        return db.insert( "observation",null,contentValuesobservation);
    }

    public int update(ObservationEntity observationDB){
        ContentValues contentValuesobservation = new ContentValues();
        contentValuesobservation.put("observation_id", observationDB.getO_ID());
        contentValuesobservation.put("observation_name", observationDB.getType());
        contentValuesobservation.put("observation_date", observationDB.getDate());
        contentValuesobservation.put("observation_comment", observationDB.getNotes());
        return db.update( "observation",contentValuesobservation, "observation_id=? and hike_id =?",
                new String[]{String.valueOf(observationDB.getO_ID()), hikeID});
    }

    public int delete(String id){
        return db.delete("observation", "observation_id=?", new String[]{String.valueOf(id)});
    }


}
