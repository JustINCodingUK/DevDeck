package dev.justincodinguk.devdeck.core.data.account

import dev.justincodinguk.devdeck.core.model.DevDeckUser
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun isSignedIn(): Boolean

    fun getCurrentUser(): Flow<DevDeckUser>

}