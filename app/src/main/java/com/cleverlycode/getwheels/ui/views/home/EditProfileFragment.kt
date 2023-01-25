package com.cleverlycode.getwheels.ui.views.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cleverlycode.getwheels.BuildConfig
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentEditProfileBinding
import com.cleverlycode.getwheels.ui.models.Profile
import com.cleverlycode.getwheels.ui.viewmodels.EditProfileViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val args: EditProfileFragmentArgs by navArgs()

    private val viewModel: EditProfileViewModel by viewModels()

    private var latestTmpUri: Uri? = null
    private lateinit var previewImage: ImageView

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

        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        binding.apply {
            previewImage = profileImage

            openCameraButton.setOnClickListener {
                takeImage()
            }

            updateProfileButton.setOnClickListener {
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
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                getImagePreview.launch(uri)
            }
        }
    }

    private val getImagePreview =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    previewImage.setImageURI(uri)
                    viewModel.updateProfilePicture(uri)
                }
            }
        }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}