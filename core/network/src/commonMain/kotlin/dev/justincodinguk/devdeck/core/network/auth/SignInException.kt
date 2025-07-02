package dev.justincodinguk.devdeck.core.network.auth

internal class SignInException(message: String) : Exception(message) {
    companion object {
        fun invalidEmail() = SignInException("No user found with this email address")
        fun invalidPassword() = SignInException("Invalid password")
        fun unknown(error: String) = SignInException(error)
    }
}