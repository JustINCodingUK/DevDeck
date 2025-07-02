package dev.justincodinguk.devdeck.core.datastore.security

data class SecurityOptions(
    val isEncryptionEnabled: Boolean,
    val encryptionKey: String?
)
