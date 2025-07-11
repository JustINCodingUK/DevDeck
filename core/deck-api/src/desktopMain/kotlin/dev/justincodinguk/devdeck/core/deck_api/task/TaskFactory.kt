package dev.justincodinguk.devdeck.core.deck_api.task

/**
 * Factory for creating [Task] instances.
 */
object TaskFactory {

    /**
     * Builds a [Task] instance using the provided [block] function.
     *
     * @param B The type of the task builder class
     * @param block The function that configures the task builder
     *
     * @return A new instance of the built task
     */
    inline fun <reified B : Task.Builder<out Task>> buildTask(block: B.() -> Unit): Task {
        val builder = B::class.java.getConstructor().newInstance()
        builder.block()
        return builder.build()
    }

}