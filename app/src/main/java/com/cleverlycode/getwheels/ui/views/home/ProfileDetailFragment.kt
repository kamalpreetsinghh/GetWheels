package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentProfileDetailBinding
import com.cleverlycode.getwheels.ui.viewmodels.ProfileDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)

        binding.apply {
            deleteProfileButton.setOnClickListener {
                viewModel.deleteAccount()
                val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                viewModel.logout(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }
        }

        return binding.root
    }
}