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

class CarsAdapter(
    private val cars: List<Car>,
    private val favButtonClick: (String, Boolean) -> Unit = { _, _ -> },
    private val showFavIcon: Boolean = false
) : RecyclerView.Adapter<CarsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(
        private val binding: CarsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        val context: Context = binding.root.context

        fun setCarDetails(car: Car) {
            binding.apply {
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
                    if (navController.currentDestination != null) {
                        val action: NavDirections = if (
                            navController.currentDestination!!.id == R.id.carsFragment
                        ) {
                            CarsFragmentDirections.actionCarsFragmentToCarDetailFragment(carId = car.id)
                        } else {
                            FavoritesFragmentDirections.actionFavoritesFragmentToCarDetailFragment(
                                carId = car.id
                            )
                        }

                        val appState = GetWheelsAppState(navController = navController)
                        appState.navigate(action)
                    }
                }

                favButton.setOnClickListener {
                    favButton.setImageResource(getFavIcon(!car.isFavorite))
                    car.id?.let { id -> favButtonClick(id, !car.isFavorite) }
                }

                Glide.with(context)
                    .load(car.imageRef)
                    .into(carImage)
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