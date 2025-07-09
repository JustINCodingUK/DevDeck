package dev.justincodinguk.devdeck.core.deck_api

import dev.justincodinguk.devdeck.core.deck_api.task.Task
import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler

data class DeckFile(
    val name: String,
    val author: String,
    val tasks: List<Task>,
    private val taskHandler: TaskHandler,
) {
    fun execute() {
        tasks.forEach { taskHandler.queueTask(it) }
    }
}
