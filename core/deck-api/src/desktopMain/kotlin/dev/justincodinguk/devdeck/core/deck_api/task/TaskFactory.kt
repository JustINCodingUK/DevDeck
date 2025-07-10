package dev.justincodinguk.devdeck.core.deck_api.task

object TaskFactory {

    inline fun <reified B : Task.Builder<out Task>> buildTask(block: B.() -> Unit): Task {
        val builder = B::class.java.getConstructor().newInstance()
        builder.block()
        return builder.build()
    }

}