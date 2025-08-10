package dev.justincodinguk.devdeck.core.data

import dev.justincodinguk.devdeck.core.data.account.AccountRepository
import dev.justincodinguk.devdeck.core.data.account.AccountRepositoryImpl
import dev.justincodinguk.devdeck.core.data.auth.AuthRepository
import dev.justincodinguk.devdeck.core.data.auth.AuthRepositoryImpl
import dev.justincodinguk.devdeck.core.data.deck.DeckRepository
import dev.justincodinguk.devdeck.core.data.deck.DeckRepositoryImpl
import dev.justincodinguk.devdeck.core.di.KoinModule
import org.koin.dsl.module

@KoinModule
val dataModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    factory<AccountRepository> { AccountRepositoryImpl(get()) }
    factory<DeckRepository> { DeckRepositoryImpl(get()) }
}