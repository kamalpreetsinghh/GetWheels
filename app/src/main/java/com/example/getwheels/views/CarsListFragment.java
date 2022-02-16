package com.example.getwheels.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getwheels.R;
import com.example.getwheels.adapters.CarsRecyclerViewAdapter;
import com.example.getwheels.databinding.FragmentCarsListBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.viewmodels.CarViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Objects;

public class CarsListFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentCarsListBinding binding;
    private CarViewModel carViewModel;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public CarsListFragment() {
    }

    public static CarsListFragment newInstance() {
        return new CarsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentCarsListBinding.inflate(inflater, container, false);
        this.carViewModel = CarViewModel.getInstance(this.requireActivity().getApplication());
        this.loadRecyclerView(this.requireActivity().getApplication());

        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.VISIBLE);

        return this.binding.getRoot();
    }

    private void loadRecyclerView(Context context) {
        MutableLiveData<List<Car>> mutableCarsList = this.carViewModel.getCarsDetails();
        mutableCarsList.observe(this.requireActivity(), cars -> {
            RecyclerView recyclerView = binding.carsRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            CarsRecyclerViewAdapter adapter = new CarsRecyclerViewAdapter(cars);
            recyclerView.setAdapter(adapter);
        });
    }

}