package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser
import dev.justincodinguk.devdeck.util.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signIn(email: String, password: String): Flow<Result<DevDeckUser>>

    fun signInWithGithub(): Flow<Result<DevDeckUser>>

    fun signInWithGithubToken(token: String): Flow<Result<DevDeckUser>>

    fun registerEmailAccount(
        email: String,
        password: String,
        displayName: String
    ): Flow<Result<DevDeckUser>>

    suspend fun signOut()

}