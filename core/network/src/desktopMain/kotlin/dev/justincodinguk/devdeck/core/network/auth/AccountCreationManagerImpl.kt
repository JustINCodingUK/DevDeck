package dev.justincodinguk.devdeck.core.network.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.justincodinguk.devdeck.core.model.AccountType
import dev.justincodinguk.devdeck.core.model.DevDeckUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.withTimeout
import java.time.Duration

internal class AccountCreationManagerImpl : AccountCreationManager {

    private val auth = Firebase.auth

    private lateinit var user: FirebaseUser

    override suspend fun beginAccountCreation(email: String, password: String): Result<Unit> {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password)
            authResult.user?.let {
                if (it.isEmailVerified) {
                    throw SignInException.userExists()
                }
                this.user = it
                it.sendEmailVerification()
            }
            return Result.success(Unit)
        } catch (e: FirebaseAuthUserCollisionException) {
            return Result.failure(e)
        }
    }

    override suspend fun onVerificationCompletion(
        displayName: String,
        timeoutMillis: Long,
        block: suspend (DevDeckUser) -> Unit
    ) {
        if (!::user.isInitialized) throw IllegalStateException("User verification requested before creation")

        withTimeout(Duration.ofMillis(timeoutMillis)) {
            while (!user.isEmailVerified) {
                user.reload()
                delay(2500)
            }
        }

        user.updateProfile(displayName = displayName)

        val newUser = DevDeckUser(
            name = displayName,
            email = user.email!!,
            photoUrl = user.photoURL,
            accountType = AccountType.DEV_DECK
        )

        block(newUser)
    }
}