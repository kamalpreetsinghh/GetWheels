package com.example.getwheels.models;

import java.util.Date;

public class Trip {
    private String userID;
    private String carID;
    private String carModel;
    private Date bookedStartDate;
    private Date bookedEndDate;
    private double price;

    public Trip() {
    }

    public Trip(String userID, String carID, String carModel, Date bookedStartDate, Date bookedEndDate, double price) {
        this.userID = userID;
        this.carID = carID;
        this.carModel = carModel;
        this.bookedStartDate = bookedStartDate;
        this.bookedEndDate = bookedEndDate;
        this.price = price;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCarModel() {
        return carModel;
    }
}
