package com.cleverlycode.getwheels.ui.views.auth

import android.content.IntentSender
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
import com.cleverlycode.getwheels.R
import com.cleverlycode.getwheels.databinding.FragmentLoginBinding
import com.cleverlycode.getwheels.ui.viewmodels.LoginViewModel
import com.cleverlycode.getwheels.utils.clickableTextSpan
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var oneTapClient: SignInClient

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appState = GetWheelsAppState(findNavController())
        viewModel.isUserSignedIn.observe(this) { isUserSignedIn ->
            if (isUserSignedIn) {
                val action = LoginFragmentDirections.actionLoginFragmentToCarsFragment()
                appState.navigate(action)
            }
        }

        oneTapClient = Identity.getSignInClient(requireActivity())

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

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.apply {
            buttonLogin.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToCarsFragment()
                viewModel.onSignInButtonClick(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            textForgotPassword.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToPasswordRecoverFragment()
                viewModel.onForgotPasswordClick(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            buttonLoginGoogle.setOnClickListener {
                oneTapSignIn()
            }

            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            val spannableText = clickableTextSpan(
                text = textCreateAccount.text.toString(),
                start = 23,
                end = 30,
                context = requireContext(),
                action = action,
                onClick = { navDirection ->
                    viewModel.onSignUpButtonClick(navDirection) { navDirections ->
                        appState.navigate(navDirections)
                    }
                }
            )

            textCreateAccount.movementMethod = LinkMovementMethod.getInstance()
            textCreateAccount.setText(spannableText, TextView.BufferType.SPANNABLE)

            lifecycleOwner = this@LoginFragment     // Set Lifecycle owner for Live Data
            viewmodel = viewModel   // Set Layout variable
        }

        return binding.root
    }

    private fun oneTapSignIn() {
        val signInRequest = createSignInRequest()

        oneTapClient
            .beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, 2,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    val error = e.message
                }
            }
            .addOnFailureListener { error ->
                val x = error
            }
    }

    private fun createSignInRequest(onlyAuthorizedAccounts: Boolean = true): BeginSignInRequest =
        BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}