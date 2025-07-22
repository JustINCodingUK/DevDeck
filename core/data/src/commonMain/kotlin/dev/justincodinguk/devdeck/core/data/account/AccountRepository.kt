package dev.justincodinguk.devdeck.core.data.account

import dev.justincodinguk.devdeck.core.model.DevDeckUser
import dev.justincodinguk.devdeck.util.Result
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    val isSignedIn: Boolean

    fun getCurrentUser(): Flow<Result<DevDeckUser>>

}