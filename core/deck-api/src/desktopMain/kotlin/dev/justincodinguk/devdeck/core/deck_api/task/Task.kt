package dev.justincodinguk.devdeck.core.deck_api.task

/** Interface representing a task that can be executed.*/
interface Task {

    /**
     * Builder interface for [Task].
     */
    interface Builder<T : Task> {
        fun build(): T
    }

    /** Name of the task. */
    val name: String

    /** Executes the task */
    suspend fun execute()
}