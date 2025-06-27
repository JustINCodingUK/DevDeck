package dev.justincodinguk.devdeck

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.justincodinguk.devdeck.core.ui.theme.DevDeckTheme
import dev.justincodinguk.devdeck.features.auth.AuthViewModel
import dev.justincodinguk.devdeck.nav.Routes
import dev.justincodinguk.devdeck.features.auth.LoginScreen
import org.koin.compose.viewmodel.koinViewModel


@Composable
@Preview
fun App() {

    val isDarkTheme by mutableStateOf(isSystemInDarkTheme())

    DevDeckTheme {
        Scaffold {
            NavHost(
                navController = rememberNavController(),
                startDestination = Routes.Login.name
            ) {
                composable(Routes.Login.name) {
                    val authViewModel = koinViewModel<AuthViewModel>()

                    LoginScreen(authViewModel, isDarkTheme)
                }
            }
        }
    }
}