package com.example.getwheels.models;

import java.util.Date;

public class Car {
    private String carID;
    private String model;
    private String color;
    private int year;
    private double price;
    private String imageUrl;
    private String owner;
    private String description;
    private String location;
    private Date bookedStartDate;
    private Date bookedEndDate;
    private String contactNumber;
    private String userID;
    private String favCarUserID;

    public Car() {
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
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

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFavCarUserID() {
        return favCarUserID;
    }

    public void setFavCarUserID(String favCarUserID) {
        this.favCarUserID = favCarUserID;
    }
}
