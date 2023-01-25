package com.cleverlycode.getwheels.ui.views.auth

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentSignUpBinding
import com.cleverlycode.getwheels.ui.viewmodels.SignUpViewModel
import com.cleverlycode.getwheels.utils.clickableTextSpan
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.apply {
            val appState = GetWheelsAppState(findNavController())

            buttonSignUp.setOnClickListener {
                val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                viewModel.onSignUpClick(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            val spannableText = clickableTextSpan(
                text = textSignIn.text.toString(),
                start = 25,
                end = 32,
                context = requireContext(),
                action = action,
                onClick = { navDirection ->
                    viewModel.onSignInClick(navDirection) { navDirections ->
                        appState.navigate(navDirections)
                    }
                }
            )

            textSignIn.movementMethod = LinkMovementMethod.getInstance()
            textSignIn.setText(spannableText, TextView.BufferType.SPANNABLE)

            lifecycleOwner = this@SignUpFragment     // Set Lifecycle owner for Live Data
            viewmodel = viewModel   // Set Layout variable
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}