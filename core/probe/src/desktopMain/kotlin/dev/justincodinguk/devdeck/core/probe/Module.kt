package dev.justincodinguk.devdeck.core.probe

import dev.justincodinguk.devdeck.core.di.KoinModule
import dev.justincodinguk.devdeck.core.probe.background.BackgroundTaskManager
import dev.justincodinguk.devdeck.core.probe.background.BackgroundTaskManagerImpl
import dev.justincodinguk.devdeck.core.probe.search.DeferredSearchController
import org.koin.dsl.module

@KoinModule
val probeModule = module {
    single<BackgroundTaskManager> { BackgroundTaskManagerImpl() }
    factory<DeferredSearchController> { DeferredSearchController(300) }
}