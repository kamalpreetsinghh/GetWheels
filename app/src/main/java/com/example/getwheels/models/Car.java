package com.example.getwheels.models;

import android.graphics.Bitmap;

import java.util.Date;

public class Car {
    private String carID;
    private String model;
    private String color;
    private int year;
    private float price;
    private Bitmap imageData;
    private String imageUrl;
    private String owner;
    private String description;
    private String location;
    private Date bookedStartDate;
    private Date bookedEndDate;

    public Car() {
    }

    public Car(String model, String color, int year, float price) {
        this.model = model;
        this.color = color;
        this.year = year;
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Bitmap getImageData() {
        return imageData;
    }

    public void setImageData(Bitmap imageData) {
        this.imageData = imageData;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getBookedStartDate() {
        return bookedStartDate;
    }

    public void setBookedStartDate(Date bookedStartDate) {
        this.bookedStartDate = bookedStartDate;
    }

    public Date getBookedEndDate() {
        return bookedEndDate;
    }

    public void setBookedEndDate(Date bookedEndDate) {
        this.bookedEndDate = bookedEndDate;
    }
}
