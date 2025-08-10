package dev.justincodinguk.devdeck.core.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import java.util.ServiceLoader

fun KoinApplication.gradleProject() : KoinApplication {
    val serviceLoaders = ServiceLoader.load(KoinModuleProvider::class.java)
    val spiLoadedModules = mutableListOf<Module>()

    serviceLoaders.forEach {
        spiLoadedModules.addAll(it.load())
    }
    return modules(spiLoadedModules)
}