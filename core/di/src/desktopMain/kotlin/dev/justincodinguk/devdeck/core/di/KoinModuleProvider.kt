package dev.justincodinguk.devdeck.core.di

import org.koin.core.module.Module

interface KoinModuleProvider {
    fun load() : List<Module>
}