package com.cleverlycode.getwheels.ui.views.home

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentEditProfileBinding
import com.cleverlycode.getwheels.ui.models.Profile
import com.cleverlycode.getwheels.ui.viewmodels.EditProfileViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val args: EditProfileFragmentArgs by navArgs()
    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var previewImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.apply {
            previewImage = profileImage

            openCameraButton.setOnClickListener {
                takeImage()
            }

            updateProfileButton.setOnClickListener {
                val bitmap = viewModel.profileUiState.value?.imageBitmap
                if (bitmap != null) {
                    saveImageToInternalStorage(fileName = "profile.jpg", bitmap = bitmap)
                }
                val action =
                    EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
                viewModel.onUpdateProfileButtonClick(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            viewModel.initializeEditProfileValues(
                profile = Profile(
                    firstName = args.firstName,
                    lastName = args.lastName,
                    email = args.email
                )
            )

            lifecycleOwner = this@EditProfileFragment
            viewmodel = viewModel
        }

        return binding.root
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            imagePreview.launch()
        }
    }

    private val imagePreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                viewModel.updateImageBitmap(bitmap)
                previewImage.setImageBitmap(bitmap)
            }
        }

    private fun saveImageToInternalStorage(
        fileName: String,
        bitmap: Bitmap
    ): Boolean {
        return try {
            val fileDirectory = requireContext().getDir("images", Context.MODE_PRIVATE)
            val file = File(fileDirectory, fileName)
            if (file.exists()) {
                file.delete()
            }

            file.createNewFile()
            file.outputStream().use { fileOutputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    throw IOException("Could not save the image")
                }
            }
            true
        } catch (exception: IOException) {
            exception.printStackTrace()
            false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}