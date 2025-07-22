package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.network.auth.AccountCreationManager
import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import dev.justincodinguk.devdeck.util.Result
import dev.justincodinguk.devdeck.util.toLiveResult
import kotlinx.coroutines.flow.flow

internal class AuthRepositoryImpl(
    private val accountManager: AccountManager,
    private val accountCreationManager: AccountCreationManager
) : AuthRepository {

    override fun signIn(email: String, password: String) = flow {
        emit(Result.Loading)
        val result = accountManager.signIn(email, password)
        emit(result.toLiveResult())
    }

    override fun signInWithGithub() = flow {
        emit(Result.Loading)
        val result = accountManager.signInWithGithub()
        emit(result.toLiveResult())
    }

    override fun signInWithGithubToken(token: String) = flow {
        emit(Result.Loading)
        val result = accountManager.signInWithGithubToken(token)
        emit(result.toLiveResult())
    }

    override fun registerEmailAccount(
        email: String,
        password: String,
        displayName: String
    ) = flow {
        emit(Result.Loading)
        with(accountCreationManager) {
            beginAccountCreation(email, password)
            onVerificationCompletion(displayName, 60*1000) { user ->
                emit(Result.Success(user))
            }
        }
    }

    override suspend fun signOut() = accountManager.signOut()

}