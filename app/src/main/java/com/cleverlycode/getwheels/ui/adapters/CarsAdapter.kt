package com.cleverlycode.getwheels.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.databinding.CarsListItemBinding
import com.cleverlycode.getwheels.domain.models.Car
import com.cleverlycode.getwheels.ui.views.home.CarsFragmentDirections
import com.cleverlycode.getwheels.ui.views.home.FavoritesFragmentDirections
import com.cleverlycode.getwheels.utils.InternalStorageUtils

class CarsAdapter(
    private val cars: List<Car>,
    private val showFavIcon: Boolean = false,
    private val favButtonClick: (String, Boolean) -> Unit = { _, _ -> },
) : RecyclerView.Adapter<CarsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(
        private val binding: CarsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        fun setCarDetails(car: Car) {
            binding.apply {
                val carId = car.id
                carCompany.text = car.company
                carName.text = car.name
                carYear.text = car.year.toString()
                carPrice.text = context.getString(R.string.price_label, car.price)
                carRatings.text = car.ratings.toString()
                carNumberOfTrips.text =
                    context.getString(R.string.number_of_trips_label, car.numberOfTrips)

                favButton.setImageResource(getFavIcon(car.isFavorite))

                if (!showFavIcon)
                    favButton.visibility = View.GONE
                else
                    favButton.visibility = View.VISIBLE

                carCard.setOnClickListener {
                    val navController = Navigation.findNavController(it)
                    val currentDestination = navController.currentDestination
                    if (currentDestination != null) {
                        val action: NavDirections = if (
                            currentDestination.id == R.id.carsFragment
                        ) {
                            CarsFragmentDirections.actionCarsFragmentToCarDetailFragment(carId = carId)
                        } else {
                            FavoritesFragmentDirections.actionFavoritesFragmentToCarDetailFragment(
                                carId = carId
                            )
                        }

                        val appState = GetWheelsAppState(navController = navController)
                        appState.navigate(action)
                    }
                }

                favButton.setOnClickListener {
                    favButton.setImageResource(getFavIcon(!car.isFavorite))
                    carId?.let { id -> favButtonClick(id, !car.isFavorite) }
                }

                if (carId != null) {
                    val imageBitmap = InternalStorageUtils.getImageFromInternalStorage(
                        context = context,
                        dirName = "cars",
                        fileName = carId
                    )
                    if (imageBitmap != null) {
                        carImage.setImageBitmap(imageBitmap)
                    } else {
                        val carImageRef = car.imageRef
                        if (carImageRef != null) {
                            Glide.with(context)
                                .load(carImageRef)
                                .into(carImage)
                        }
                    }
                }
            }
        }

        private fun getFavIcon(isFavorite: Boolean): Int =
            if (isFavorite)
                R.drawable.icon_fav_fill
            else
                R.drawable.icon_not_fav

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CarsListItemBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setCarDetails(cars[position])
    }

    override fun getItemCount(): Int = cars.size
}