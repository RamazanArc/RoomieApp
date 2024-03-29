package com.example.roomieapp.model;

import java.util.List;

public class Item {

    private String location;
    private String price;
    private String description;
    private String shortDescription;
    private String imageUrl;
    private String currentUserID;
    private String ilanID;

    public String getIlanID() {
        return ilanID;
    }

    public void setIlanID(String ilanID) {
        this.ilanID = ilanID;
    }

    public Item(String description, String shortDescription, String price, String location, String currentUserID, String imageUrl,String ilanID) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.currentUserID = currentUserID;
        this.imageUrl = imageUrl;
        this.ilanID = ilanID;
    }

    public Item() {

    }

    public Item(String location, String price, String shortDescription) {
        this.location = location;
        this.price = price;
        this.shortDescription = shortDescription;
    }

    public Item(String description, String shortDescription, String price, String location) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public Item(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
