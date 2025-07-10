package dev.justincodinguk.devdeck.core.deck_api.task

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Implementation of [Task] to clone a repository into a directory.
 *
 * @param repository The repository to clone
 * @param directory The directory to clone the repository into
 * @param gitInstallTask The task to execute if git is not installed
 */
class CloneTask(
    private val repository: String,
    private val directory: String,
    private val gitInstallTask: InstallTask,
) : Task {

    override val name = "Clone $repository into $directory"

    private val logger = LoggerFactory.getLogger("CloneTask")

    /**
     * Executes the [CloneTask]. Clones the repository into the specified directory, and installs git if it's not installed
     */
    override suspend fun execute() {
        logger.info(name)
        withContext(Dispatchers.IO) {
            File(directory).mkdirs()
            val process = ProcessBuilder("git clone https://github.com/$repository".split(" "))
                .redirectErrorStream(true)
                .directory(File(directory))
                .start()
            val exitCode = process.waitFor()
            if(exitCode==127) {
                gitInstallTask.execute()
                execute()
            }
        }
    }
}