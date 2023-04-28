package com.cleverlycode.getwheels.ui.views.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cleverlycode.getwheels.GetWheelsAppState
import com.cleverlycode.getwheels.databinding.FragmentAuthBinding
import com.cleverlycode.getwheels.ui.viewmodels.AuthViewModel
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appState = GetWheelsAppState(findNavController())

        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding.apply {
            buttonLogin.setOnClickListener {
                val action = AuthFragmentDirections.actionAuthFragmentToLoginFragment()
                viewModel.signInButtonClick(action) { navDirections ->
                    appState.navigate(navDirections)
                }
            }

            buttonSignUp.setOnClickListener {
                val action = AuthFragmentDirections.actionAuthFragmentToSignUpFragment()
                viewModel.signUpButtonClick(action) {navDirections ->
                    appState.navigate(navDirections)
                }
            }

            lifecycleOwner = this@AuthFragment
        }

        return binding.root
    }
}