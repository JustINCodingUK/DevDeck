package dev.justincodinguk.devdeck.features.auth

import dev.justincodinguk.devdeck.core.di.KoinModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@KoinModule
val authFeatureModule = module {
    viewModel<AuthViewModel> { AuthViewModel(get(), get()) }
}