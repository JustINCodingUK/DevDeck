package dev.justincodinguk.devdeck

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.justincodinguk.devdeck.di.KoinModuleLoader
import dev.justincodinguk.devdeck.firebase.Firebase
import org.koin.compose.KoinApplication


fun main() = application {

    Firebase.init()

    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    KoinApplication(
        application = {
            modules(KoinModuleLoader.allModules())
        }
    ) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "DevDeck",
            resizable = false
        ) {
            App()
        }
    }
}