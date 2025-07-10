package dev.justincodinguk.devdeck.core.deck_api.task

import kotlinx.coroutines.Job

/**
 * Represents a process that is running in the background.
 *
 * @param id The unique identifier of the process
 * @param task The [Task] that is being executed
 * @param job The [Job] associated with the process
 */
data class Process(
    val id: Int,
    val task: Task,
    val job: Job
) {
    /** Stops the process */
    fun stop() = job.cancel()
}
