package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.ext.runAsCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Implementation of [Task] to run a command in a project directory.
 *
 * @param command The command to run
 * @param projectDir The directory to run the command in
 */
class RunTask(
    private val command: String,
    private val projectDir: String
) : Task {

    override val name = "Run ${command.split(" ").first()}"

    private val logger = LoggerFactory.getLogger("RunTask")

    /**
     * Executes the [RunTask]. Runs the specified command in the specified directory.
     */
    override suspend fun execute() {
        logger.info(name)
        val isWindows = System.getProperty("os.name").startsWith("Windows")
        if (isWindows) command.removePrefix("./")
        withContext(Dispatchers.IO) {
            command.runAsCommand(File(projectDir))
        }
    }
}