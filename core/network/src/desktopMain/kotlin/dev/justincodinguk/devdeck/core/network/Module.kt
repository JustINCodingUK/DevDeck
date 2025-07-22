package dev.justincodinguk.devdeck.core.network

import dev.justincodinguk.devdeck.core.di.KoinModule
import dev.justincodinguk.devdeck.core.network.auth.AccountCreationManager
import dev.justincodinguk.devdeck.core.network.auth.AccountCreationManagerImpl
import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import dev.justincodinguk.devdeck.core.network.auth.AccountManagerImpl
import org.koin.dsl.module

@KoinModule
val networkModule = module {
    single<AccountManager> { AccountManagerImpl() }
    factory<AccountCreationManager> { AccountCreationManagerImpl() }
}