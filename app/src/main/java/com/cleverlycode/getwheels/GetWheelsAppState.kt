package com.cleverlycode.getwheels

import androidx.navigation.NavController
import androidx.navigation.NavDirections

class GetWheelsAppState(
    var navController: NavController,
) {
    fun navigate(action: NavDirections) {
        navController.navigate(action)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun navigateAndPopBackStack(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            navController.popBackStack()
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}