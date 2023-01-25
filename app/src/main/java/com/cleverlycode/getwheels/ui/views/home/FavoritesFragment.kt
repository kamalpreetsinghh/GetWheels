package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.ui.adapters.CarsAdapter
import com.cleverlycode.getwheels.databinding.FragmentFavoritesBinding
import com.cleverlycode.getwheels.ui.viewmodels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        viewModel.cars.observe(viewLifecycleOwner) { cars ->
            binding.favRecyclerView.adapter =
                CarsAdapter(
                    cars = cars
                )
        }

        return binding.root
    }
}