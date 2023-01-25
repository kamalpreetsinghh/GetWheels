package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentProfileBinding
import com.cleverlycode.getwheels.ui.viewmodels.ProfileViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val storageRef = viewModel.getProfilePictureReference()

        binding.apply {
            viewmodel?.getProfilePictureReference()?.observe(viewLifecycleOwner) { storageRef ->
                Glide.with(requireContext())
                    .load(storageRef)
                    .into(profileImage)
            }

            Glide.with(requireContext())
                .load(storageRef.value)
                .into(profileImage)


            logoutButton.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                viewmodel?.logout(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            profileImage.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToProfileDetailFragment()
                viewmodel?.navigateToProfileDetails(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            editProfileButton.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(
                    firstName = viewModel.profileUiState.value?.firstName ?: "",
                    lastName = viewModel.profileUiState.value?.lastName ?: "",
                    email = viewModel.profileUiState.value?.email ?: ""
                )
                viewmodel?.navigateToEditProfile(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            lifecycleOwner = this@ProfileFragment
            viewmodel = viewModel
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}