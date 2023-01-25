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
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.databinding.FragmentCarDetailBinding
import com.cleverlycode.getwheels.ui.adapters.SliderAdapter
import com.cleverlycode.getwheels.ui.viewmodels.CarDetailsViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CarDetailFragment : Fragment() {
    private var _binding: FragmentCarDetailBinding? = null
    private val binding get() = _binding!!

    private val args: CarDetailFragmentArgs by navArgs()

    private val viewModel: CarDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val carId = args.carId
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        carId?.let { viewModel.getCarDetails(it) }

        viewModel.imageRefs.observe(viewLifecycleOwner) {
            binding.viewPager.adapter = SliderAdapter(it)
        }

        binding.apply {
            calendarButton.setOnClickListener {
                openDatePicker()
            }

            bookCarButton.setOnClickListener {
                val fromDate = viewModel.fromDate.value
                val toDate = viewModel.toDate.value
                if(fromDate != null && toDate != null) {
                    val action =
                        CarDetailFragmentDirections.actionCarDetailFragmentToCarBookingFragment(
                            carId,
                            viewModel.fromDate.value.toString(),
                            viewModel.toDate.value.toString()
                        )
                    viewModel.navigateToCarBooking(action) { navDirections ->
                        appState.navigate(navDirections)
                    }
                } else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.select_date_dialog_title))
                        .setMessage(resources.getString(R.string.select_date_dialog_message))
                        .setNegativeButton("Cancel") { _, _ ->
                        }
                        .setPositiveButton("OK") { _, _ ->
                            openDatePicker()
                        }
                        .show()
                }
            }

            lifecycleOwner = this@CarDetailFragment
            viewmodel = viewModel
        }

        return binding.root
    }

    private fun openDatePicker() {
        val datePicker = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText(resources.getString(R.string.date_picker_title))
            .build()

        datePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER_RANGE")

        datePicker.addOnPositiveButtonClickListener {
            viewModel.setFromDate(it.first)
            viewModel.setToDate(it.second)
            viewModel.showDates.value = true
        }
    }
}