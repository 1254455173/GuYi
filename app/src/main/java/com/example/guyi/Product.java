package com.example.guyi;

/**
 * Created by 陈 on 2020/7/9.
 */

public class Product {
    private String name;

    private int imageId;

    private double price;

    private String location;

    private String short_info;

    private String long_info;

    public Product(String name, int imageId, String location, double price){
        this.name = name;
        this.imageId = imageId;
        this.location = location;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

    public String getLong_info() {
        return long_info;
    }

    public void setLong_info(String long_info) {
        this.long_info = long_info;
    }
}
