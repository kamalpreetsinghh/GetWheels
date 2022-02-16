package com.example.getwheels.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.getwheels.R;
import com.example.getwheels.databinding.FragmentCarDetailsBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.models.Trip;
import com.example.getwheels.viewmodels.TripViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import java.util.Date;
import java.util.Objects;

public class CarDetailsFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentCarDetailsBinding binding;

    private FirebaseAuth mAuth;
    private TripViewModel tripViewModel;
    private Car car;
    private Date startDate;
    private Date endDate;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);
        navBar.setVisibility(View.GONE);

        this.binding = FragmentCarDetailsBinding.inflate(inflater, container, false);
        this.loadCarDetails();

        mAuth = FirebaseAuth.getInstance();
        this.tripViewModel = TripViewModel.getInstance(this.requireActivity().getApplication());
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Car Booking Dates")
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        this.binding.bookingButton.setOnClickListener(view -> {
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            double pricePerDay = this.car.getPrice();
            int numberOfDays = this.endDate.getDay() - this.startDate.getDay();
            double price = pricePerDay * numberOfDays;
            Trip trip = new Trip(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), this.car.getCarID(), this.car.getModel(), this.startDate, this.endDate, price);

            this.tripViewModel.addTrip(trip);
        });

        this.binding.imageButtonNavigate.setOnClickListener(view -> {

        });

        this.binding.imageButtonDial.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + this.car.getContactNumber()));
            startActivity(intent);
        });

        this.binding.imageButtonCalendar.setOnClickListener(view -> {
            materialDatePicker.show(getParentFragmentManager(), materialDatePicker.toString());

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                startDate = new Date(selection.first);
                endDate = new Date(selection.second);
            });
        });

        this.binding.imageButtonFav.setOnClickListener(view -> {

        });

        return this.binding.getRoot();
    }

    private void loadCarDetails() {
        this.binding.textViewCarModel.setText(this.car.getModel() + " " + this.car.getYear());
        this.binding.textViewCarPriceDetail.setText("$" + this.car.getPrice() + " /day");
        this.binding.textViewOwnerName.setText(this.car.getOwner());
        this.binding.textViewLocation.setText(this.car.getLocation());
        this.binding.textViewDescription.setText(this.car.getDescription());
        this.binding.textViewContactNumber.setText(this.car.getContactNumber());

        Picasso.get().load(this.car.getImageUrl())
                .into(this.binding.imageViewCarDetail);

    }

}