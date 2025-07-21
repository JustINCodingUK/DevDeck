package dev.justincodinguk.devdeck.features.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser

data class AuthState(
    val emailText: String = "",
    val passwordText: String = "",
    val displayNameText: String = "",
    val authStatus: AuthStatus = AuthStatus.NOT_LOGGED_IN,
    val authMode: AuthMode = AuthMode.LOGIN,
    val user: DevDeckUser? = null,
)

enum class AuthStatus {
    NOT_LOGGED_IN,
    WAITING,
    VERIFICATION,
    LOGGED_IN
}

enum class AuthMode {
    LOGIN,
    REGISTER
}
