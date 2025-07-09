package dev.justincodinguk.devdeck.core.deck_api.task


class StepTask(
    taskName: String,
    val tasks: List<Task>
) : Task {

    override val name = "Executing stepped task: $taskName"

    override suspend fun execute() {
        println(name)
        tasks.forEach { it.execute() }
    }
}