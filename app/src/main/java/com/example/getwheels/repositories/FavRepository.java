package com.example.getwheels.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.getwheels.models.Car;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavRepository {
    private final String COLLECTION_NAME = "favorites";
    private final String TAG = this.getClass().getCanonicalName();

    private final FirebaseFirestore db;
    private final CollectionReference collection;
    public MutableLiveData<List<Car>> cars = new MutableLiveData<>();

    public FavRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = this.db.collection(this.COLLECTION_NAME);
    }

    public void getFavCars(String userID) {
        this.collection.whereEqualTo("favCarUserID", userID)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }
                    if (snapshots != null && !snapshots.isEmpty()) {
                        ArrayList<Car> carsList = new ArrayList<>();
                        for (QueryDocumentSnapshot snapshot : snapshots) {
                            Car car = snapshot.toObject(Car.class);
                            carsList.add(car);
                        }
                        cars.postValue(carsList);
                    }
                });
    }

    public void addToFav(Car car) {
        this.collection.document().set(car);
        this.db.collection("cars").document(car.getCarID()).update("favCarUserID", car.getFavCarUserID());
    }
}
