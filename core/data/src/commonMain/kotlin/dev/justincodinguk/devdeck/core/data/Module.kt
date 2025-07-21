package dev.justincodinguk.devdeck.core.data

import dev.justincodinguk.devdeck.core.data.account.AccountRepository
import dev.justincodinguk.devdeck.core.data.account.AccountRepositoryImpl
import dev.justincodinguk.devdeck.core.data.auth.AuthRepository
import dev.justincodinguk.devdeck.core.data.auth.AuthRepositoryImpl
import dev.justincodinguk.devdeck.core.di.KoinModuleProvider
import org.koin.dsl.module

val dataModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory<AccountRepository> { AccountRepositoryImpl(get()) }
}

class DataModuleInit : KoinModuleProvider {
    override fun load() = listOf(dataModule)
}