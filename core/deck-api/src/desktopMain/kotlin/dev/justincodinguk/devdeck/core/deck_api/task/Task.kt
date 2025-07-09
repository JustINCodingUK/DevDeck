package dev.justincodinguk.devdeck.core.deck_api.task

interface Task {
    val name: String

    suspend fun execute()
}