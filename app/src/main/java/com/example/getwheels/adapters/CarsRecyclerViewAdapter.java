package com.example.getwheels.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.getwheels.R;
import com.example.getwheels.models.Car;
import com.example.getwheels.views.CarDetailsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.ViewHolder>{
    private final String TAG = this.getClass().getCanonicalName();
    private List<Car> mCars;
    private LayoutInflater mInflater;
    private AdapterView.OnItemClickListener mClickListener;
    private final FirebaseStorage storage;

    public CarsRecyclerViewAdapter(Context context, List<Car> cars) {
        this.mInflater = LayoutInflater.from(context);
        this.mCars = cars;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public CarsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_car_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarsRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getTextViewCarModel().setText(mCars.get(viewHolder.getAdapterPosition()).getModel() + " " + mCars.get(viewHolder.getAdapterPosition()).getYear());
        viewHolder.getTextViewCarPrice().setText("$" + mCars.get(viewHolder.getAdapterPosition()).getPrice() + "/day");

        this.storage.getReference().child(mCars.get(viewHolder.getAdapterPosition()).getCarID() + "/carimage.jpg").getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Picasso.get().load(mCars.get(position).getImageUrl()).resize(360, 170).centerCrop()
                                .into(viewHolder.getImageViewCar()))
                .addOnFailureListener(exception -> {
                });
    }

    @Override
    public int getItemCount() {
        return this.mCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCarModel;
        private final TextView textViewCarPrice;
        private final ImageView imageViewCar;

        public ViewHolder(View view) {
            super(view);

            view.setOnClickListener(v -> {
                Log.d(TAG, "Element " + ViewHolder.this.getAdapterPosition() + " clicked.");
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new CarDetailsFragment(mCars.get(getAdapterPosition()))).addToBackStack(null).commit();
            }
            );

            textViewCarModel = (TextView) view.findViewById(R.id.recyclerViewCarModel);
            textViewCarPrice = (TextView) view.findViewById(R.id.recyclerViewPrice);
            imageViewCar = (ImageView) view.findViewById(R.id.imageViewCar);
        }

        public TextView getTextViewCarModel() {
            return textViewCarModel;
        }

        public TextView getTextViewCarPrice() {
            return textViewCarPrice;
        }

        public ImageView getImageViewCar() {
            return imageViewCar;
        }

    }
}
