package com.example.getwheels.repositories;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.getwheels.models.Car;
import com.example.getwheels.models.Trip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripsRepository {
    private final String COLLECTION_NAME = "trips";
    private final String TAG = this.getClass().getCanonicalName();

    private final FirebaseFirestore db;
    private final CollectionReference collection;
    public MutableLiveData<List<Trip>> trips = new MutableLiveData<>();

    public TripsRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = this.db.collection(this.COLLECTION_NAME);
    }

    public void getUserTrips(String userID) {
        this.collection.whereEqualTo("userID", userID)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (snapshots != null && !snapshots.isEmpty()) {
                        ArrayList<Trip> tripsList = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : snapshots) {
                            Trip trip = snapshot.toObject(Trip.class);
                            tripsList.add(trip);
                        }
                        trips.postValue(tripsList);
                    }
                });
    }

    public void addTrip(Trip trip) {
        this.collection.document().set(trip);
        Map<String, Object> updates = new HashMap<>();
        updates.put("userID", trip.getUserID());
        updates.put("bookedStartDate", trip.getBookedStartDate());
        updates.put("bookedEndDate", trip.getBookedEndDate());
        this.db.collection("cars").document(trip.getCarID()).update(updates);
    }

    public void getTripDetails(String userID, String carID) {
        this.collection.whereEqualTo("userID", userID)
                .whereEqualTo("carID", carID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            collection.document(document.getId()).delete();
                            db.collection("cars").document(carID).update("favCarUserID", null);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}
