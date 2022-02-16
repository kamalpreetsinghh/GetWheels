package com.example.getwheels.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.getwheels.R;
import com.example.getwheels.adapters.CarsRecyclerViewAdapter;
import com.example.getwheels.databinding.ActivityLoginBinding;
import com.example.getwheels.databinding.ActivityTestBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.viewmodels.CarViewModel;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getCanonicalName();
    private ActivityTestBinding binding;

    private CarViewModel carViewModel;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding =ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.carViewModel = CarViewModel.getInstance(this.getApplication());
        this.loadImage();
    }

    private void loadImage() {
        MutableLiveData<List<Car>> mutableCarsList = this.carViewModel.getCarsDetails();
        mutableCarsList.observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(List<Car> cars) {
                String carUrl = "https://firebasestorage.googleapis.com/v0/b/getwheels-1ea8e.appspot.com/o/jpUH3Y6B54EFQGzRXCH8%2Fcarimage.jpg?alt=media&token=37444931-55bb-42cb-b92c-195b11534ec6";
                Picasso.get()
                        .load(carUrl)
                        .into(binding.testImage);

            }
        });
    }
}