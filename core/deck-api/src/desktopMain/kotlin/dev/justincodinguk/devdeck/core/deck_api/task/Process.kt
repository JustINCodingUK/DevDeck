package dev.justincodinguk.devdeck.core.deck_api.task

import kotlinx.coroutines.Job

data class Process(
    val id: Int,
    val task: Task,
    val job: Job
) {
    fun stop() = job.cancel()
}
