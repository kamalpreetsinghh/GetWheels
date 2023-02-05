package com.cleverlycode.getwheels.data.remote

import com.google.firebase.auth.FirebaseUser

interface AccountService {
    val currentUser: FirebaseUser?
    val currentUserId: String
    suspend fun createAccount(email: String, password: String): String?
    suspend fun signInWithEmailAndPassword(email: String, password: String)
    suspend fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun signOut()
    suspend fun sendRecoveryEmail(email: String)
}