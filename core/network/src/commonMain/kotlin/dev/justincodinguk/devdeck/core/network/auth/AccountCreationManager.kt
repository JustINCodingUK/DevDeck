package dev.justincodinguk.devdeck.core.network.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser

interface AccountCreationManager {

    suspend fun beginAccountCreation(email: String, password: String): Result<Unit>

    suspend fun onVerificationCompletion(
        displayName: String,
        timeoutMillis: Long,
        block: suspend (DevDeckUser) -> Unit
    )

}