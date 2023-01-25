package com.cleverlycode.getwheels

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cleverlycode.getwheels.databinding.ActivityMainBinding
import com.cleverlycode.getwheels.ui.viewmodels.CarsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CarsViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragmentView: View = binding.navHostFragment
        navHostFragmentView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarsInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.statusBars())
            } else {
                TODO("VERSION.SDK_INT < R")
            }
            view.updatePadding(top = statusBarsInsets.top)
            insets
        }

        val bottomNavigationView: BottomNavigationView = binding.bottomNavView
        bottomNavigationView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarsInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.navigationBars())
            } else {
                TODO("VERSION.SDK_INT < R")
            }
            view.updatePadding(bottom = statusBarsInsets.bottom)
            insets
        }

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value == false
        }

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        setupBottomNavBar()
    }

    private fun setupBottomNavBar() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
            if (navDestination.id == R.id.carsFragment || navDestination.id == R.id.favoritesFragment ||
                navDestination.id == R.id.tripsFragment || navDestination.id == R.id.profileFragment
            ) {
                bottomNavigationView.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
            }
        }
    }
}