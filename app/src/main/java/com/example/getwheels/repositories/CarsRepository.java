package com.example.getwheels.repositories;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.example.getwheels.models.Car;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class CarsRepository {
    private final String COLLECTION_NAME = "cars";
    private final String TAG = this.getClass().getCanonicalName();

    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    private final CollectionReference collection;
    public MutableLiveData<List<Car>> cars = new MutableLiveData<>();

    public CarsRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.collection = this.db.collection(this.COLLECTION_NAME);
    }

    public void getCarsDetails() {
        this.collection
                .addSnapshotListener((snapshots, error) -> {
                if (error != null) {
                    Log.e(TAG, "getUserDetails: ", error);
                    return;
                }
                if (snapshots != null && !snapshots.isEmpty()) {
                    List<Car> carsList = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : snapshots) {
                        Car car = snapshot.toObject(Car.class);
                        car.setCarID(snapshot.getReference().getId());

                       this.storage.getReference().child(snapshot.getReference().getId() + "/carimage.jpg").getDownloadUrl()
                               .addOnSuccessListener(uri -> car.setImageUrl(uri.toString()))
                               .addOnFailureListener(exception -> { });

                        carsList.add(car);
                    }
                    cars.postValue(carsList);
                }
            });
    }

}
