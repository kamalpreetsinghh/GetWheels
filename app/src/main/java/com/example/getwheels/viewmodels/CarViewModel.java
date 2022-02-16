package com.example.getwheels.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.getwheels.models.Car;
import com.example.getwheels.repositories.CarsRepository;
import com.example.getwheels.repositories.FavRepository;

import java.util.List;

public class CarViewModel extends AndroidViewModel {
    private static CarViewModel instance;
    private final CarsRepository carsRepository = new CarsRepository();
    private final FavRepository favRepository = new FavRepository();

    public CarViewModel(@NonNull Application application) {
        super(application);
    }

    public static CarViewModel getInstance(Application application){
        if (instance == null){
            instance = new CarViewModel(application);
        }
        return instance;
    }

    public MutableLiveData<List<Car>> getCarsDetails(){
        this.carsRepository.getCarsDetails();
        return carsRepository.cars;
    }

    public MutableLiveData<List<Car>> getFavCars(String userID){
        this.favRepository.getFavCars(userID);
        return favRepository.cars;
    }

    public void addToFav(Car car) {
        this.favRepository.addToFav(car);
    }
}
