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
import dev.justincodinguk.devdeck.features.auth.AuthScreen
import dev.justincodinguk.devdeck.nav.Dashboard
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val isDarkTheme by mutableStateOf(isSystemInDarkTheme())

    DevDeckTheme {
        Scaffold {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Routes.Auth.name
            ) {
                composable(Routes.Auth.name) {
                    AuthScreen(isDarkTheme) { navController.navigate(Routes.Dashboard.name) }
                }

                composable(Routes.Dashboard.name) {
                    Dashboard()
                }
            }
        }
    }
}