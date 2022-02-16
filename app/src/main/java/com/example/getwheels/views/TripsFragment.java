package com.example.getwheels.views;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getwheels.R;
import com.example.getwheels.adapters.CarsRecyclerViewAdapter;
import com.example.getwheels.adapters.TripsRecyclerViewAdapter;
import com.example.getwheels.databinding.FragmentCarsListBinding;
import com.example.getwheels.databinding.FragmentTripsBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.models.Trip;
import com.example.getwheels.viewmodels.CarViewModel;
import com.example.getwheels.viewmodels.TripViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;
import java.util.Objects;

public class TripsFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentTripsBinding binding;
    private TripViewModel tripViewModel;
    private FirebaseAuth mAuth;

    public TripsFragment() { }

    public static TripsFragment newInstance() {
        return new TripsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        this.binding = FragmentTripsBinding.inflate(inflater, container, false);
        this.tripViewModel = TripViewModel.getInstance(this.requireActivity().getApplication());
        this.loadRecyclerView(this.requireActivity().getApplication());
        return this.binding.getRoot();
    }

    private void loadRecyclerView(Context context) {
        MutableLiveData<List<Trip>> mutableTripsList = this.tripViewModel.getUserTrips(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        mutableTripsList.observe(this.requireActivity(), trips -> {
            RecyclerView recyclerView = binding.tripsRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            TripsRecyclerViewAdapter adapter = new TripsRecyclerViewAdapter(context, trips);
            recyclerView.setAdapter(adapter);
        });
    }
}