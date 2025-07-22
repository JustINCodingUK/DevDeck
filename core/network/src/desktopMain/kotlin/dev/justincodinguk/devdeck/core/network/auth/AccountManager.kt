package dev.justincodinguk.devdeck.core.network.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser

interface AccountManager {
    fun isSignedIn(): Boolean

    fun getCurrentUser(): Result<DevDeckUser>

    suspend fun signIn(email: String, password: String) : Result<DevDeckUser>

    suspend fun signInWithGithubToken(token: String) : Result<DevDeckUser>

    suspend fun signInWithGithub() : Result<DevDeckUser>

    suspend fun signOut()
}