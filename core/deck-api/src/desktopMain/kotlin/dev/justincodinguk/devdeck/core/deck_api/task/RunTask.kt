package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.ext.runAsCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RunTask(
    private val command: String,
    private val projectDir: String
) : Task {

    override val name = "Run ${command.split(" ").first()}"

    override suspend fun execute() {
        println(name)
        val isWindows = System.getProperty("os.name").startsWith("Windows")
        if (isWindows) command.removePrefix("./")
        withContext(Dispatchers.IO) {
            command.runAsCommand(File(projectDir))
        }
    }

}