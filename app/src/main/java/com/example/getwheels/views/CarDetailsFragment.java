package com.example.getwheels.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import com.example.getwheels.R;
import com.example.getwheels.databinding.FragmentCarDetailsBinding;
import com.example.getwheels.models.Car;
import com.example.getwheels.models.Trip;
import com.example.getwheels.viewmodels.CarViewModel;
import com.example.getwheels.viewmodels.TripViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CarDetailsFragment extends Fragment {
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentCarDetailsBinding binding;

    private String currentUserID;
    private TripViewModel tripViewModel;
    private CarViewModel carViewModel;
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

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.currentUserID = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        this.tripViewModel = TripViewModel.getInstance(this.requireActivity().getApplication());
        this.carViewModel = CarViewModel.getInstance(this.requireActivity().getApplication());

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Car Booking Dates")
                .setSelection(Pair.create(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds()))
                .build();

        this.binding.bookingButton.setOnClickListener(view -> {

            if(this.car.getUserID() != null && this.car.getUserID().equals(this.currentUserID)) {
                Toast.makeText(getActivity(), "This car is already booked by you",
                        Toast.LENGTH_SHORT).show();
            } else if ((this.car.getBookedStartDate() == null && this.car.getBookedEndDate() == null) ||
                    (this.car.getBookedStartDate().compareTo(this.endDate) < 0 || this.car.getBookedEndDate().compareTo(this.startDate) > 0)) {
                long daysInMilli = 1000 * 60 * 60 * 24;
                double pricePerDay = this.car.getPrice();
                int numberOfDays = (int) Math.abs((this.endDate.getTime() - this.startDate.getTime())/daysInMilli);
                double tripPrice = pricePerDay * numberOfDays;

                Trip trip = new Trip(this.currentUserID, this.car.getCarID(),
                        this.car.getModel(), this.startDate, this.endDate, tripPrice);

                this.tripViewModel.addTrip(trip);

                Toast.makeText(getActivity(), "Car Booked",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "This car is not available for the selected dates. Please choose another dates or choose a different car.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        this.binding.imageButtonNavigate.setOnClickListener(view -> {
//            if(ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this.requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this.requireActivity(), new ArrayList<String>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                        ,requestcode
//            }
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
            if (this.car.getFavCarUserID() != null && this.car.getFavCarUserID().equals(this.currentUserID)) {
                Toast.makeText(getActivity(), "Already added to favourites",
                        Toast.LENGTH_SHORT).show();
            } else {
                this.car.setFavCarUserID(this.currentUserID);
                this.carViewModel.addToFav(this.car);
                Toast.makeText(getActivity(), "Added to Favorites",
                        Toast.LENGTH_SHORT).show();
            }
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