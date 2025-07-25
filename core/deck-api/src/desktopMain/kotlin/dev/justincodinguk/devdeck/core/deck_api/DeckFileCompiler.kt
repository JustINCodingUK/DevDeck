package dev.justincodinguk.devdeck.core.deck_api

import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReference
import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReferenceLoader
import dev.justincodinguk.devdeck.core.deck_api.refs.Platform
import dev.justincodinguk.devdeck.core.deck_api.task.CloneTask
import dev.justincodinguk.devdeck.core.deck_api.task.InstallTask
import dev.justincodinguk.devdeck.core.deck_api.task.ReferenceTask
import dev.justincodinguk.devdeck.core.deck_api.task.RunTask
import dev.justincodinguk.devdeck.core.deck_api.task.StepTask
import dev.justincodinguk.devdeck.core.deck_api.task.Task
import dev.justincodinguk.devdeck.core.deck_api.task.TaskFactory
import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.properties.Delegates

/**
 * Compiles and parses a `.deckfile` file to produce a [DeckFile] object containing executable tasks.
 *
 * This class powers the DevDeck DSL system, enabling automated environment setup from a
 * declarative script. It handles script parsing, task generation, and platform-specific
 * resolution of install references.
 *
 * @param filePath Path to the `.deckfile` file containing the setup script.
 * @param fileName Optional name of the deckfile.
 * @param projectVariables Optional map of project variables to be used in the script.
 * @param referencesDirectory Directory where installation references are stored or cached.
 * @param taskBatchSize Number of tasks to run in parallel.
 */
class DeckFileCompiler private constructor(
    private val filePath: String,
    private val fileName: String? = null,
    private val projectVariables: Map<String, String> = mapOf(),
    referencesDirectory: String,
    taskBatchSize: Int
) {
    private val deckFileRaw = File(filePath).readLines()

    /** [TaskHandler] object to manage running tasks */
    private val taskHandler = TaskHandler(taskBatchSize)

    private val jsonSerializer = Json { ignoreUnknownKeys = true; prettyPrint = true }

    /** Ktor [HttpClient] to make get requests */
    private val httpClient = HttpClient(CIO) {
        engine {
            requestTimeout = 0
        }
        install(ContentNegotiation) {
            json(jsonSerializer)
        }
    }

    /** [InstallReferenceLoader] object to load local and online installation references */
    private val installReferenceLoader =
        InstallReferenceLoader(referencesDirectory, httpClient, jsonSerializer)

    /** Current [Platform] with OS and architecture */
    private val platform = Platform.current()

    /**
     * Loads and compiles the deck file into a [DeckFile] object with executable tasks.
     * `deck.name` and `deck.author` are optional.
     *
     * @return [DeckFile] object containing executable tasks.
     * @throws DeckFileSyntaxException when the deckfile has invalid syntax.
     */
    fun loadDeckFile(): DeckFile {
        var scriptStarted = false
        var deckName = "Unspecified"
        var deckAuthor = "Unspecified"
        val tasks = mutableListOf<Task>()
        var step = false
        var stepTaskName = ""
        val stepTasks = mutableListOf<Task>()

        deckFileRaw.forEach { rawLine ->
            var line = rawLine.trim()
            projectVariables.forEach {
                line = line.replace("\$${it.key}", it.value)
            }
            try {
                if (!scriptStarted) {
                    if (line.startsWith("deck.name")) {
                        deckName = line.split("=")[1]
                    } else if (line.startsWith("deck.author")) {
                        deckAuthor = line.split("=")[1]
                    } else if (line == "deck.start") scriptStarted = true
                } else {
                    if (line.isNotEmpty()) {
                        if (line.lowercase().startsWith("step begin")) {
                            step = true
                            stepTasks.clear()
                            stepTaskName = line.trim().split(" ")[2]
                        } else if (line.lowercase().startsWith("step end")) {
                            step = false
                            val stepTask = TaskFactory.buildTask<StepTask.Builder> {
                                taskName = stepTaskName
                                this.tasks = stepTasks.toList()
                            }
                            tasks.add(stepTask)
                            stepTasks.clear()
                        } else {
                            val parsedTasks = parseTasks(line)
                            if (step) {
                                stepTasks.addAll(parsedTasks)
                            } else {
                                tasks.addAll(parsedTasks)
                            }
                        }
                    }
                }
            } catch (e: IndexOutOfBoundsException) {
                throw DeckFileSyntaxException(filePath, line)
            }
        }

        return DeckFile(fileName ?: deckName, deckAuthor, tasks, taskHandler)
    }

    /**
     * Parses [Task] objects present in a single line of the deckfile
     *
     * @param line A single line of the deckfile
     * @return List of parsed [Task] objects
     * @throws [DeckFileSyntaxException] when the line has invalid syntax
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun parseTasks(line: String): List<Task> {
        val splitLine = line.split(" ")
        val command = splitLine.first().lowercase()
        val args = splitLine.subList(1, splitLine.size)

        when (command) {
            // Example syntax: INSTALL company.artifactId@version
            "install" -> {
                val refsWithVersion = buildMap {
                    args.forEach {
                        if ("@" in it) {
                            val (id, version) = it.split("@")
                            put(installReferenceLoader.getReference(id), version)
                        }
                        if ("@" !in it) {
                            put(installReferenceLoader.getReference(it), "")
                        }
                    }
                }

                val pkgRefs = refsWithVersion.keys.toList().packageManagerReferences()
                val tasks = mutableListOf<Task>()
                if (pkgRefs.isNotEmpty()) {
                    val packageManagerInstallTask =
                        TaskFactory.buildTask<InstallTask.MultiplePackagesBuilder> {
                            platform = this@DeckFileCompiler.platform
                            installReferences = pkgRefs
                            httpClient = this@DeckFileCompiler.httpClient
                        }
                    tasks.add(packageManagerInstallTask)
                }

                refsWithVersion.keys.toList().urlReferences()
                    .forEach {
                        val installTask = TaskFactory.buildTask<InstallTask.Builder> {
                            platform = this@DeckFileCompiler.platform
                            installReference = it
                            version = refsWithVersion[it]!!
                            httpClient = this@DeckFileCompiler.httpClient
                        }

                        tasks += installTask
                    }
                return tasks
            }

            // Example syntax: CLONE owner/repo_name IN /path/to/folder
            "clone" -> {
                val url = args.first()
                var dir = ""
                if (args.size != 1) {
                    dir = args[2]
                }

                val gitInstallTask = TaskFactory.buildTask<InstallTask.Builder> {
                    platform = this@DeckFileCompiler.platform
                    installReferenceLoader.getReference("git.git")
                    version = ""
                    httpClient = this@DeckFileCompiler.httpClient
                } as InstallTask

                val cloneTask = TaskFactory.buildTask<CloneTask.Builder> {
                    repository = url
                    directory = dir
                    this.gitInstallTask = gitInstallTask
                }
                return listOf(cloneTask)
            }

            // Example syntax: RUN "command --args" IN /path/to/folder
            "run" -> {
                val runningCommand = line.split('"')[1]
                var dir = ""
                if (args.size != 1) {
                    dir = args.last()
                }

                val runTask = TaskFactory.buildTask<RunTask.Builder> {
                    this.command = runningCommand
                    projectDir = dir
                }
                return listOf(runTask)
            }

            // Example syntax: REFERENCE https://url.to/installrefs.json
            "reference" -> {
                val url = args.first()
                val refTask = TaskFactory.buildTask<ReferenceTask.Builder> {
                    this.url = url
                    this.installReferenceLoader = this@DeckFileCompiler.installReferenceLoader
                }
                return listOf(refTask)
            }

            else -> {
                throw DeckFileSyntaxException(filePath, line)
            }
        }
    }

    /**
     * Utility extension function to filter installations which would proceed via the system package manager
     * @return List of [InstallReference] objects which would proceed via the system package manager
     */
    private fun List<InstallReference>.packageManagerReferences() = filter {
        !(it.getReferenceResultForPlatform(platform).isUrl)
    }

    /**
     * Utility extension function to filter installations which would require manual installation and setting up of environment variables and menu entries
     * @return List of [InstallReference] objects which would proceed manually
     */
    private fun List<InstallReference>.urlReferences() = filter {
        (it.getReferenceResultForPlatform(platform).isUrl)
    }

    /**
     * Builder class for [DeckFileCompiler]
     */
    class Builder {
        var fileName: String? = null
        var projectVariables: Map<String, String> = mapOf()
        lateinit var filePath: String
        lateinit var referencesDirectory: String
        var taskBatchSize by Delegates.notNull<Int>()

        fun build() = DeckFileCompiler(filePath, fileName, projectVariables, referencesDirectory, taskBatchSize)
    }

}

/**
 * DSL function to build a [DeckFileCompiler] object
 * @param block Lambda function to configure the [DeckFileCompiler.Builder]
 * @return [DeckFileCompiler] object with the specified configuration
 */
fun deckFileCompiler(block: DeckFileCompiler.Builder.() -> Unit) : DeckFileCompiler {
    val builder = DeckFileCompiler.Builder()
    builder.block()
    return builder.build()
}