package com.example.getwheels.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.getwheels.models.Car;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void removeFromFav(String userID, String carID) {
        this.collection.whereEqualTo("favCarUserID", userID)
                .whereEqualTo("carID", carID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int tasks = task.getResult().getDocuments().size();
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
