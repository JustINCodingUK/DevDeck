package dev.justincodinguk.devdeck.di

import dev.justincodinguk.devdeck.core.di.KoinModuleProvider
import org.koin.core.module.Module
import java.util.ServiceLoader

object KoinModuleLoader {
    fun allModules() : List<Module> {
        val providers = ServiceLoader.load(KoinModuleProvider::class.java)
        return providers.flatMap {
            it.load()
        }
    }
}