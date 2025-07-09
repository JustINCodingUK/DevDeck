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
import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception

class DeckFileCompiler(
    private val filePath: String,
    referencesDirectory: String,
    taskBatchSize: Int
) {

    private val deckFileRaw = File(filePath).readLines()
    val taskHandler = TaskHandler(taskBatchSize)
    private val jsonSerializer = Json { ignoreUnknownKeys = true; prettyPrint = true }
    private val httpClient = HttpClient(CIO) {
        engine {
            requestTimeout = 0
        }
        install(ContentNegotiation) {
            json(jsonSerializer)
        }
    }
    private val installReferenceLoader =
        InstallReferenceLoader(referencesDirectory, httpClient, jsonSerializer)
    private val platform = Platform.current()

    fun loadDeckFile(): DeckFile {
        var scriptStarted = false
        var deckName = "Unspecified"
        var deckAuthor = "Unspecified"
        val tasks = mutableListOf<Task>()
        var step = false
        var stepTaskName = ""
        val stepTasks = mutableListOf<Task>()

        deckFileRaw.forEach {
            val line = it.trim()
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
                            val stepTask = StepTask(stepTaskName, stepTasks.toList())
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
            } catch (e: Exception) {
                throw DeckFileSyntaxException(filePath, line)
            }
        }

        return DeckFile(deckName, deckAuthor, tasks, taskHandler)
    }

    private fun parseTasks(line: String): List<Task> {
        val splitLine = line.split(" ")
        val command = splitLine.first().lowercase()
        val args = splitLine.subList(1, splitLine.size)

        when (command) {
            "install" -> {
                val refsWithVersion = buildMap {
                    args.forEach {
                        if("@" in it) {
                            val (id, version) = it.split("@")
                            put(installReferenceLoader.getReference(id), version)
                        }
                        if("@" !in it) {
                            put(installReferenceLoader.getReference(it), "")
                        }
                    }
                }

                val pkgRefs = refsWithVersion.keys.toList().packageManagerReferences()
                val tasks = mutableListOf<Task>()
                if (pkgRefs.isNotEmpty()) {
                    val packageManagerInstallTask =
                        InstallTask.multiplePackages(
                            platform,
                            pkgRefs,
                            httpClient
                        )
                    tasks.add(packageManagerInstallTask)
                }

                refsWithVersion.keys.toList().urlReferences()
                    .forEach {
                        tasks += InstallTask(platform, it, refsWithVersion[it]!!, httpClient)
                    }
                return tasks
            }

            "clone" -> {
                val url = args.first()
                var dir = ""
                if (args.size != 1) {
                    dir = args[2]
                }
                val cloneTask = CloneTask(url, dir, installReferenceLoader)
                return listOf(cloneTask)
            }

            "run" -> {
                val runningCommand = line.split('"')[1]
                var dir = ""
                if (args.size != 1) {
                    dir = args.last()
                }
                val runTask = RunTask(runningCommand, dir)
                return listOf(runTask)
            }

            "reference" -> {
                val url = args.first()
                return listOf(ReferenceTask(url, installReferenceLoader))
            }

            else -> {
                throw DeckFileSyntaxException(filePath, line)
            }
        }
    }

    private fun List<InstallReference>.packageManagerReferences() = filter {
        !(it.getReferenceResultForPlatform(platform).isUrl)
    }

    private fun List<InstallReference>.urlReferences() = filter {
        (it.getReferenceResultForPlatform(platform).isUrl)
    }

}