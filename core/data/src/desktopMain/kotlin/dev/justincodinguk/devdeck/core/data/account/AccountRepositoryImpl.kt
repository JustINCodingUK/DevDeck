package dev.justincodinguk.devdeck.core.data.account

import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import dev.justincodinguk.devdeck.util.Result
import dev.justincodinguk.devdeck.util.toLiveResult
import kotlinx.coroutines.flow.flow

internal class AccountRepositoryImpl(
    private val accountManager: AccountManager
) : AccountRepository {

    override val isSignedIn = accountManager.isSignedIn()

    override fun getCurrentUser() = flow {
        emit(Result.Loading)
        val currentUser = accountManager.getCurrentUser()
        emit(currentUser.toLiveResult())
    }

}