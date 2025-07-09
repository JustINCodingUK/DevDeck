package dev.justincodinguk.devdeck.features.auth

import dev.justincodinguk.devdeck.core.di.KoinModuleProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authFeatureModule = module {
    viewModel<AuthViewModel> { AuthViewModel(get()) }
}

class AuthFeatureModuleInit : KoinModuleProvider {
    override fun load() = listOf(authFeatureModule)
}