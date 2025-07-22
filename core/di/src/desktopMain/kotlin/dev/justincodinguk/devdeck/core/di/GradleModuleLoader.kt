package dev.justincodinguk.devdeck.core.di

import org.koin.core.KoinApplication
import org.koin.core.module.Module
import java.util.ServiceLoader

fun KoinApplication.gradleProject(): List<Module> {
    val serviceLoaders = ServiceLoader.load(KoinModuleProvider::class.java)
    val modules = mutableListOf<Module>()

    serviceLoaders.forEach {
        modules.addAll(it.load())
    }
    return modules
}