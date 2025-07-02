package dev.justincodinguk.devdeck.features.auth

import dev.justincodinguk.devdeck.core.model.DevDeckUser

data class AuthState(
    val emailText: String = "",
    val passwordText: String = "",
    val authStatus: AuthStatus = AuthStatus.NOT_LOGGED_IN,
    val user: DevDeckUser? = null,
)

enum class AuthStatus {
    NOT_LOGGED_IN,
    WAITING,
    LOGGED_IN
}
