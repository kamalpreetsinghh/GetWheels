package com.example.getwheels.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.getwheels.R;
import com.example.getwheels.databinding.FragmentCarDetailsBinding;
import com.example.getwheels.models.Car;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

public class CarDetailsFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentCarDetailsBinding binding;
    private Car car;

    public CarDetailsFragment() { }

    public CarDetailsFragment(Car car) {
        this.car = car;
    }

    public static CarDetailsFragment newInstance() {
        return new CarDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        BottomNavigationView navBar = getActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.GONE);

        this.binding = FragmentCarDetailsBinding.inflate(inflater, container, false);
        this.loadCarDetails();

        this.binding.bookingButton.setOnClickListener(view -> {
           AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new BookingFragment(this.car.getCarID())).addToBackStack(null).commit();
        });

        this.binding.imageButtonNavigate.setOnClickListener(view -> {

        });

        return this.binding.getRoot();
    }

    private void loadCarDetails() {
        this.binding.textViewCarModel.setText(this.car.getModel() + this.car.getYear());
        this.binding.textViewCarPriceDetail.setText("$" + this.car.getPrice() + " /day");
        this.binding.textViewOwnerName.setText(this.car.getOwner());
        this.binding.textViewLocation.setText(this.car.getLocation());
        this.binding.textViewDescription.setText(this.car.getDescription());

        Picasso.get().load(this.car.getImageUrl())
                .into(this.binding.imageViewCarDetail);

    }

}