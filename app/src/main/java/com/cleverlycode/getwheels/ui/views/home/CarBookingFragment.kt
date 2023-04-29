package com.cleverlycode.getwheels.ui.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentCarBookingBinding
import com.cleverlycode.getwheels.domain.models.BookingDetails
import com.cleverlycode.getwheels.ui.viewmodels.CarBookingViewModel
import com.cleverlycode.getwheels.utils.InternalStorageUtils
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFade
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.time.LocalDate

@AndroidEntryPoint
class CarBookingFragment : Fragment() {
    private val url = "https://getwheelsstripeapi.onrender.com/payment-sheet"
    private var volleyResponse = MutableLiveData("")

    private lateinit var paymentSheet: PaymentSheet
    private lateinit var customerConfig: PaymentSheet.CustomerConfiguration
    private lateinit var paymentIntentClientSecret: String

    private var _binding: FragmentCarBookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarBookingViewModel by viewModels()

    private val args: CarBookingFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchStripeApiCredentials()
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        enterTransition = MaterialFade()
        reenterTransition = MaterialFade()
    }

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
                presentPaymentSheet()
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

            val nameObserver = Observer<String> { newName ->
                // Update the UI, in this case, a TextView.
                volley.text = newName
            }

            volleyResponse.observe(
                viewLifecycleOwner,
                nameObserver
            )

            lifecycleOwner = this@CarBookingFragment
            viewmodel = viewModel
        }

        return binding.root
    }

    private fun fetchStripeApiCredentials() {
        val queue = Volley.newRequestQueue(this.context)
        val stringRequest = StringRequest(
            Request.Method.POST,
            url,
            { response ->
                val responseJson = JSONObject(response)
                paymentIntentClientSecret = responseJson.getString("paymentIntent")
                customerConfig = PaymentSheet.CustomerConfiguration(
                    responseJson.getString("customer"),
                    responseJson.getString("ephemeralKey")
                )
                val publishableKey = responseJson.getString("publishableKey")
                PaymentConfiguration.init(this.requireContext(), publishableKey)
            },
            { val error = "That didn't work!" }
        )

        queue.add(stringRequest)
    }

    private fun presentPaymentSheet() {
        paymentSheet.presentWithPaymentIntent(
            paymentIntentClientSecret,
            PaymentSheet.Configuration(
                merchantDisplayName = "Get Wheels",
                customer = customerConfig,
                allowsDelayedPaymentMethods = false
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                print("Canceled")
            }
            is PaymentSheetResult.Failed -> {
                print("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                print("Completed")
            }
        }
    }
}