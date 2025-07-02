package dev.justincodinguk.devdeck.core.data.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun isSignedIn(): Boolean

    fun getCurrentUser(): Flow<DevDeckUser>

    fun signIn(email: String, password: String) : Flow<DevDeckUser>

    fun signInWithGithub() : Flow<DevDeckUser>

    fun signInWithGithubToken(token: String) : Flow<DevDeckUser>

    suspend fun signOut()

}