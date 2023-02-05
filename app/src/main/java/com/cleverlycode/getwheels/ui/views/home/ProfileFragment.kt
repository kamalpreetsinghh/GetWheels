package com.cleverlycode.getwheels.ui.views.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.UserViewModel
import com.cleverlycode.getwheels.databinding.FragmentProfileBinding
import com.cleverlycode.getwheels.ui.viewmodels.ProfileViewModel
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            if(userViewModel.isUserLoggedIn) {
                val userId = userViewModel.userID
                val bitmap = getImageFromInternalStorage(fileName = "profile.jpg")
                if (bitmap != null) {
                    profileImage.setImageBitmap(bitmap)
                } else {
                    runBlocking {
                        val bitmapFromFirestore = viewModel.getProfilePictureAsync().await()
                        if (bitmapFromFirestore != null) {
                            profileImage.setImageBitmap(bitmapFromFirestore)
                        }
                    }
                }
            }

            logoutButton.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                viewmodel?.logout(action) { navDirections ->
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

    private fun getImageFromInternalStorage(fileName: String): Bitmap? {
        val fileDirectory = requireContext().getDir("images", Context.MODE_PRIVATE)
        return try {
            val fileInputStream = File(fileDirectory, fileName).inputStream()
            BitmapFactory.decodeStream(fileInputStream)
        } catch (exception: Exception) {
            if (exception !is FileNotFoundException) {
                File(fileDirectory, fileName).delete()
            }
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}