package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cleverlycode.getwheels.ui.adapters.CarsAdapter
import com.cleverlycode.getwheels.databinding.FragmentCarsBinding
import com.cleverlycode.getwheels.ui.viewmodels.CarsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarsFragment : Fragment() {
    private var _binding: FragmentCarsBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: CarsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarsBinding.inflate(inflater, container, false)

        viewmodel.cars.observe(viewLifecycleOwner) { carsList ->
            binding.carsRecyclerView.adapter =
                CarsAdapter(
                    cars = carsList,
                    favButtonClick = { carId, isFavorite ->
                        viewmodel.favButtonClick(carId, isFavorite)
                    },
                    showFavIcon = true
                )
        }

        return binding.root
    }
}