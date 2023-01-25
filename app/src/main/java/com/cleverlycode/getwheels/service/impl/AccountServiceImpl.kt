package com.cleverlycode.getwheels.service.impl

import com.cleverlycode.getwheels.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val currentUserId: String
        get() = currentUser?.uid.orEmpty()

    override suspend fun createAccount(email: String, password: String): String? =
        auth.createUserWithEmailAndPassword(email, password)
            .await().user?.uid

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}