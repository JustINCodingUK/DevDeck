package dev.justincodinguk.devdeck.core.model

data class DevDeckUser(
    val name: String,
    val email: String,
    val photoUrl: String?,
    val accountType: AccountType
)