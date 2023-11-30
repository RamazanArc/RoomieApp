package com.example.roomieapp.model;

import java.util.List;

public class Item {

    private String location;
    private String price;
    private List<Integer> images;
    private String description;
    private String shortDescription;
    private int totalImages;

    public Item() {

    }

    public Item(String location, String price, String shortDescription) {
        this.location = location;
        this.price = price;
        this.shortDescription = shortDescription;
    }

    public Item(String location, String price, List<Integer> images, String description, String shortDescription, int totalImages) {
        this.location = location;
        this.price = price;
        this.images = images;
        this.description = description;
        this.shortDescription = shortDescription;
        this.totalImages = totalImages;
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

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
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

    public int getTotalImages() {
        return totalImages;
    }

    public void setTotalImages(int totalImages) {
        this.totalImages = totalImages;
    }
}
