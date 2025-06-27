package dev.justincodinguk.devdeck

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.justincodinguk.devdeck.features.auth.authModule
import org.koin.compose.KoinApplication

fun main() = application {

    val windowState = rememberWindowState(placement = WindowPlacement.Maximized)

    KoinApplication(
        application = {
            modules(authModule)
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