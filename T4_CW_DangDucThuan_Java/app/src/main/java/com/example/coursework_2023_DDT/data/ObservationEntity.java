package com.example.coursework_2023_DDT.data;

import com.example.coursework_2023_DDT.Convention;

import org.json.JSONObject;

import java.util.HashMap;

public class ObservationEntity {
    private String O_ID;
    private String type;
    private String date;
    private String notes;
    private String H_ID;

    public ObservationEntity(String type, String date, String notes, String H_ID) {
        this.O_ID = Convention.NEWSS_HIKE_IDSS;
        this.type = type;
        this.date = date;
        this.notes = notes;
        this.H_ID = H_ID;
    }

    public ObservationEntity(String O_ID, String type, String date, String notes, String H_ID) {
        setO_ID(O_ID);
        setType(type);
        setDate(date);
        setNotes(notes);
        setH_ID(H_ID);
    }
    public ObservationEntity() {
        this(
                Convention.NEWSS_HIKE_IDSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS
        );
    }

    public String getO_ID() {
        return O_ID;
    }

    public void setO_ID(String O_ID) {
        this.O_ID = O_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getH_ID() {
        return H_ID;
    }

    public void setH_ID(String H_ID) {
        this.H_ID = H_ID;
    }

    @Override
    public String toString() {
        return "ObservationEntity{" +
                "O_ID='" + O_ID + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                ", H_ID='" + H_ID + '\'' +
                '}';
    }

    public JSONObject toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("date", date);
        map.put("notes", notes);
        map.put("H_ID", H_ID);
        return new JSONObject(map);
    }
}
