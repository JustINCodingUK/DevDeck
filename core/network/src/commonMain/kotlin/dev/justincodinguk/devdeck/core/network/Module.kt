package dev.justincodinguk.devdeck.core.network

import dev.justincodinguk.devdeck.core.di.KoinModuleProvider
import dev.justincodinguk.devdeck.core.network.auth.AccountManager
import dev.justincodinguk.devdeck.core.network.auth.AccountManagerImpl
import org.koin.dsl.module

val networkModule = module {
    single<AccountManager> { AccountManagerImpl() }
}

class NetworkModuleInit : KoinModuleProvider {
    override fun load() = listOf(networkModule)
}