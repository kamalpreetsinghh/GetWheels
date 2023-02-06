package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cleverlycode.getwheels.UserViewModel
import com.cleverlycode.getwheels.databinding.FragmentFavoritesBinding
import com.cleverlycode.getwheels.ui.adapters.CarsAdapter
import com.cleverlycode.getwheels.ui.viewmodels.FavoritesViewModel
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private val sharedViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        if (sharedViewModel.isUserLoggedIn) {
            viewModel.cars.observe(viewLifecycleOwner) { cars ->
                binding.favRecyclerView.adapter =
                    CarsAdapter(
                        cars = cars
                    )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val navController = findNavController()
//        if (!sharedViewModel.isUserLoggedIn) {
//            val action = FavoritesFragmentDirections.actionFavoritesFragmentToLoginFragment()
//            val appState = GetWheelsAppState(navController)
//            appState.navigate(action = action)
//        }

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            navController.popBackStack(R.id.carsFragment, false)
//        }
    }
}