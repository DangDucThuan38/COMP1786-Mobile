package com.example.applogbook3.model;

public class Data {
    private String id,name,email,date;
    private byte[] image;
    public Data(){

    }
    public Data(String id, String name, String email,String date,byte[] image){
        this.id=id;
        this.name=name;
        this.email=email;
        this.date=date;
        this.image=image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

