package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signIn(email: String, password: String): Flow<DevDeckUser>

    fun signInWithGithub(): Flow<DevDeckUser>

    fun signInWithGithubToken(token: String): Flow<DevDeckUser>

    fun registerEmailAccount(
        email: String,
        password: String,
        displayName: String
    ): Flow<DevDeckUser>

    suspend fun signOut()

}