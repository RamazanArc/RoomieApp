package com.example.roomieapp.model;

public class User {
    private String name;
    private String email;
    private String image;
    private String staus;
    private String userID;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public User(){

    }


    public User(String name, String email, String userID,String staus) {
        this.name = name;
        this.email = email;
        this.userID = userID;
        this.staus = staus;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
