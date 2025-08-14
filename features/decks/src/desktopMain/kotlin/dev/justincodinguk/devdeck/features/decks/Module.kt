package dev.justincodinguk.devdeck.features.decks

import dev.justincodinguk.devdeck.core.di.KoinModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@KoinModule
val decksFeatureModule = module {
    viewModelOf(::DeckViewModel)
}