package com.cleverlycode.getwheels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.cleverlycode.getwheels.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    accountService: AccountService,
) : ViewModel() {
    val isUserLoggedIn = accountService.currentUser != null
    val userID: String = accountService.currentUserId
}