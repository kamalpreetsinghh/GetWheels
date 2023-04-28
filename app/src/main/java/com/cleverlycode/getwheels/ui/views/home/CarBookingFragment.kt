package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentCarBookingBinding
import com.cleverlycode.getwheels.domain.models.BookingDetails
import com.cleverlycode.getwheels.ui.viewmodels.CarBookingViewModel
import com.cleverlycode.getwheels.utils.InternalStorageUtils
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class CarBookingFragment : Fragment() {
    private var _binding: FragmentCarBookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarBookingViewModel by viewModels()

    private val args: CarBookingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        args.carId?.let {
            viewModel.setBookingDetails(
                it,
                LocalDate.parse(args.fromDate),
                LocalDate.parse(args.toDate)
            )
        }

        _binding = FragmentCarBookingBinding.inflate(inflater, container, false)

        binding.apply {
            checkoutButton.setOnClickListener {
                viewModel.saveBookingDetails()
            }

            buttonBack.setOnClickListener {
                viewModel.navigateBack {
                    appState.navigateUp()
                }
            }

            args.carId?.let {
                rentalCarImage.setImageBitmap(
                    InternalStorageUtils.getImageFromInternalStorage(
                        context = binding.root.context,
                        dirName = "cars",
                        fileName = it
                    )
                )
            }

            lifecycleOwner = this@CarBookingFragment
            viewmodel = viewModel
        }

        return binding.root
    }
}