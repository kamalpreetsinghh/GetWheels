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
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        val navHostFragmentView: View = binding.navHostFragment
        navHostFragmentView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarsInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.statusBars()).top
            } else {
                0
            }
            view.updatePadding(top = statusBarsInsets)
            insets
        }

        val bottomNavigationView: BottomNavigationView = binding.bottomNavView
        bottomNavigationView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarsInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insets.getInsets(WindowInsets.Type.navigationBars()).bottom
            } else {
                0
            }
            view.updatePadding(bottom = statusBarsInsets)
            insets
        }

        installSplashScreen()
//            .setKeepOnScreenCondition {
//            true    // Put condition for keeping splash screen
//        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController
        setupBottomNavBar()

        viewModel.syncWithRemoteDataSource(context = applicationContext)

        setContentView(binding.root)
    }

    private fun setupBottomNavBar() {
        val bottomNavigationView: BottomNavigationView = binding.bottomNavView
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