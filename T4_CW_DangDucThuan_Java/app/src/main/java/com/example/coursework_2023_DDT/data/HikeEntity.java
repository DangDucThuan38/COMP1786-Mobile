package com.example.coursework_2023_DDT.data;

import com.example.coursework_2023_DDT.Convention;

import java.util.HashMap;
import java.util.Map;


public class HikeEntity {
    private String id;
    private String name;
    private String destination;
    private String date;
    private int participant;
    private String description;
    private String parking;
    private String level;
    private String length;
    private String location;

    public HikeEntity() {
        this(
                Convention.NEWSS_HIKE_IDSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                0,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS,
                Convention.EMPTYSS_STRINGSS
        );
    }

    public HikeEntity(String name, String destination, String date, int participant, String description,
                      String parking, String level, String length, String location) {
        this(
                Convention.NEWSS_HIKE_IDSS,
                name,
                destination,
                date,
                participant,
                description,
                parking,
                level,
                length,
                location
        );
    }

    public HikeEntity(String id, String name, String destination, String date, int participant, String description,
                      String parking, String level, String length, String location) {
        setId(id);
        setName(name);
        setDestination(destination);
        setDate(date);
        setParticipant(participant);
        setDescription(description);
        setParking(parking);
        setLevel(level);
        setLength(length);
        setLocation(location);
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public int getParticipant() {
        return participant;
    }

    public String getDescription() {
        return description;
    }

    public String getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> getMapWithoutId() {
        Map<String, Object> hikeMap = new HashMap<>();
        hikeMap.put("name", this.name);
        hikeMap.put("description", this.description);
        hikeMap.put("destination", this.destination);
        hikeMap.put("date", this.date);
        hikeMap.put("participant", this.participant);
        hikeMap.put("parking", this.parking);
        hikeMap.put("level", this.level);
        hikeMap.put("length", this.length);
        hikeMap.put("location", this.location);
        return hikeMap;
    }
}
