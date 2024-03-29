package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cleverlycode.getwheels.SharedViewModel
import com.cleverlycode.getwheels.databinding.FragmentCarsBinding
import com.cleverlycode.getwheels.ui.adapters.CarsAdapter
import com.cleverlycode.getwheels.ui.viewmodels.CarsViewModel
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarsFragment : Fragment() {
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: CarsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarsBinding.inflate(inflater, container, false)
        sharedViewModel.cars.observe(viewLifecycleOwner) { cars ->
            binding.carsRecyclerView.adapter =
                CarsAdapter(
                    cars = cars,
                    showFavIcon = true
                ) { carId, isFavorite ->
                    viewmodel.favButtonClick(carId, isFavorite)
                }
        }

        return binding.root
    }
}