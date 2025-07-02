package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import kotlinx.coroutines.flow.flow

internal class AuthRepositoryImpl(
    private val accountManager: AccountManager
) : AuthRepository {
    override fun isSignedIn() = accountManager.isSignedIn()

    override fun getCurrentUser() = flow {
        val currentUser = accountManager.getCurrentUser()
        emit(currentUser.getOrThrow())
    }

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

    override suspend fun signOut() = accountManager.signOut()

}