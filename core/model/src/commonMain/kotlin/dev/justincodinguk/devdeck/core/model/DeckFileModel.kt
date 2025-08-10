package dev.justincodinguk.devdeck.core.model

data class DeckFileModel(
    val id: String,
    val author: DevDeckUser,
    val name: String,
    val contents: List<String>
)