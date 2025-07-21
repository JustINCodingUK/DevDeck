package dev.justincodinguk.devdeck.core.data.account

import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import kotlinx.coroutines.flow.flow

class AccountRepositoryImpl(
    private val accountManager: AccountManager
) : AccountRepository {

    override fun isSignedIn() = accountManager.isSignedIn()

    override fun getCurrentUser() = flow {
        val currentUser = accountManager.getCurrentUser()
        emit(currentUser.getOrThrow())
    }

}