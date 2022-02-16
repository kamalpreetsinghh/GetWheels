package com.example.getwheels.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.getwheels.models.Trip;
import com.example.getwheels.repositories.TripsRepository;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private static TripViewModel instance;
    private final TripsRepository tripsRepository = new TripsRepository();

    public TripViewModel (@NonNull Application application) {
        super(application);
    }

    public static TripViewModel getInstance(Application application){
        if (instance == null){
            instance = new TripViewModel(application);
        }
        return instance;
    }

    public MutableLiveData<List<Trip>> getUserTrips(String userID){
        this.tripsRepository.getUserTrips(userID);
        return tripsRepository.trips;
    }

    public void addTrip(Trip trip) {
        this.tripsRepository.addTrip(trip);
    }
}
