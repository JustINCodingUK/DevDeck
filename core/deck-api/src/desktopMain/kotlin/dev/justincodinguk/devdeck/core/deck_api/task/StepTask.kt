package dev.justincodinguk.devdeck.core.deck_api.task

import org.slf4j.LoggerFactory

/**
 * Implementation of [Task] to execute a list of tasks in the specified order.
 *
 * @param taskName The name of the task sequence
 * @param tasks The list of tasks to execute synchronously
 */
class StepTask private constructor(
    taskName: String,
    private val tasks: List<Task>
) : Task {

    override val name = "Executing stepped task: $taskName"

    private val logger = LoggerFactory.getLogger("StepTask")

    /**
     * Executes the [StepTask]. Executes the tasks in the specified order.
     */
    override suspend fun execute() {
        logger.info(name)
        tasks.forEach { it.execute() }
    }

    /**
     * Builder class for [StepTask].
     */
    class Builder : Task.Builder<StepTask> {
        lateinit var taskName: String
        lateinit var tasks: List<Task>

        override fun build() = StepTask(taskName, tasks)
    }
}