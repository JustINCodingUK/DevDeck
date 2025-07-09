package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReferenceLoader
import dev.justincodinguk.devdeck.core.deck_api.refs.Platform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

class CloneTask(
    private val repository: String,
    private val directory: String,
    private val installReferenceLoader: InstallReferenceLoader,
) : Task {

    override val name = "Clone $repository into $directory"

    override suspend fun execute() {
        println(name)
        withContext(Dispatchers.IO) {
            File(directory).mkdirs()
            val process = ProcessBuilder("git clone https://github.com/$repository".split(" "))
                .redirectErrorStream(true)
                .directory(File(directory))
                .start()
            val exitCode = process.waitFor()
            if(exitCode==127) {
                installGit()
                execute()
            }
            delay(2000)
        }
    }

    private suspend fun installGit() {
        val gitInstallationReference = installReferenceLoader.getReference("git.git")
    }
}