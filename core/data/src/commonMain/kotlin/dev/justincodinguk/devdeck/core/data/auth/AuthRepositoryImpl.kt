package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.network.auth.AccountCreationManager
import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import kotlinx.coroutines.flow.flow

internal class AuthRepositoryImpl(
    private val accountManager: AccountManager,
    private val accountCreationManager: AccountCreationManager
) : AuthRepository {

    override fun signIn(email: String, password: String) = flow {
        val result = accountManager.signIn(email, password)
        emit(result.getOrThrow())
    }

    override fun signInWithGithub() = flow {
        val result = accountManager.signInWithGithub()
        emit(result.getOrThrow())
    }

    override fun signInWithGithubToken(token: String) = flow {
        val result = accountManager.signInWithGithubToken(token)
        emit(result.getOrThrow())
    }

    override fun registerEmailAccount(
        email: String,
        password: String,
        displayName: String
    ) = flow {
        with(accountCreationManager) {
            beginAccountCreation(email, password)
            onVerificationCompletion(displayName, 60*1000) { user ->
                emit(user)
            }
        }
    }

    override suspend fun signOut() = accountManager.signOut()

}