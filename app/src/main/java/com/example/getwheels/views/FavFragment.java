package com.example.getwheels.views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getwheels.R;
import com.example.getwheels.adapters.CarsRecyclerViewAdapter;
import com.example.getwheels.databinding.FragmentCarsListBinding;
import com.example.getwheels.databinding.FragmentFavBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.viewmodels.CarViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Objects;

public class FavFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentFavBinding binding;
    private CarViewModel carViewModel;
    private FirebaseAuth mAuth;

    public FavFragment() { }

    public static FavFragment newInstance() {
        return new FavFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentFavBinding.inflate(inflater, container, false);
        this.carViewModel = CarViewModel.getInstance(this.requireActivity().getApplication());
        mAuth = FirebaseAuth.getInstance();
        this.loadRecyclerView(this.requireActivity().getApplication());
       return this.binding.getRoot();
    }

    private void loadRecyclerView(Context context) {
        MutableLiveData<List<Car>> mutableCarsList = this.carViewModel.getFavCars(Objects.requireNonNull(this.mAuth.getCurrentUser()).getUid());
        mutableCarsList.observe(this.requireActivity(), cars -> {
            RecyclerView recyclerView = binding.favRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            CarsRecyclerViewAdapter adapter = new CarsRecyclerViewAdapter(cars);
            recyclerView.setAdapter(adapter);
        });
    }
}