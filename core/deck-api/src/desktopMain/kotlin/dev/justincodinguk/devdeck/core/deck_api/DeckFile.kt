package dev.justincodinguk.devdeck.core.deck_api

import dev.justincodinguk.devdeck.core.deck_api.task.Task
import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler

/**
 * Object representation of a .deckfile
 *
 * @param name Name of the deckfile
 * @param author Name of the author of the deckfile
 * @param tasks List of [Task] objects to be executed
 * @param taskHandler [TaskHandler] object to manage execution of tasks
 */
data class DeckFile(
    val name: String,
    val author: String,
    val tasks: List<Task>,
    val taskHandler: TaskHandler,
) {

    /** Executes the tasks in the .deckfile, with the batch size specified in the [DeckFileCompiler] object */
    fun execute() {
        tasks.forEach { taskHandler.queueTask(it) }
    }
}
