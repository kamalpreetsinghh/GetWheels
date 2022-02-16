package com.example.getwheels.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.getwheels.R;
import com.example.getwheels.models.Trip;
import com.example.getwheels.views.CarDetailsFragment;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TripsRecyclerViewAdapter extends RecyclerView.Adapter<TripsRecyclerViewAdapter.ViewHolder>{
    private final String TAG = this.getClass().getCanonicalName();
    private final FirebaseStorage storage;
    private final List<Trip> trips;

    public TripsRecyclerViewAdapter(Context context, List<Trip> trips) {
        this.trips = trips;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public TripsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_trip_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        Date bookingStartDate = trips.get(viewHolder.getAdapterPosition()).getBookedStartDate();
        Date bookingEndDate = trips.get(viewHolder.getAdapterPosition()).getBookedEndDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        String formattedDate = dateFormat.format(bookingStartDate) + " - " + dateFormat.format(bookingEndDate);


        viewHolder.getTextViewTripModel().setText(trips.get(viewHolder.getAdapterPosition()).getCarModel());
        viewHolder.getTextViewTripPrice().setText("$" + trips.get(viewHolder.getAdapterPosition()).getPrice());
        viewHolder.getTextViewTripDates().setText(formattedDate);

        this.storage.getReference().child(trips.get(viewHolder.getAdapterPosition()).getCarID() + "/carimage.jpg").getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Picasso.get().load(uri).resize(360, 170).centerCrop()
                                .into(viewHolder.getImageViewTripCar()))
                .addOnFailureListener(exception -> {
                });
    }

    @Override
    public int getItemCount() {
        return this.trips.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTripModel;
        private final TextView textViewTripPrice;
        private final ImageView imageViewTripCar;
        private final TextView textViewTripDates;

        public ViewHolder(View view) {
            super(view);

            textViewTripModel = (TextView) view.findViewById(R.id.textViewTripModel);
            textViewTripPrice = (TextView) view.findViewById(R.id.textViewTripPrice);
            imageViewTripCar = (ImageView) view.findViewById(R.id.imageViewTripCar);
            textViewTripDates = (TextView) view.findViewById(R.id.textViewTripDates);

        }

        public TextView getTextViewTripModel() {
            return textViewTripModel;
        }

        public TextView getTextViewTripPrice() {
            return textViewTripPrice;
        }

        public ImageView getImageViewTripCar() {
            return imageViewTripCar;
        }

        public TextView getTextViewTripDates() {
            return textViewTripDates;
        }
    }
}

