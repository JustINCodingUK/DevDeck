# Deck API

![Maven Central Version](https://img.shields.io/maven-central/v/io.github.justincodinguk.devdeck/deck-api)

Compiler and Tasks API for deckfiles

All tasks must implement [Task](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/Task.kt)

### Usage
Use the `deckFileCompiler` DSL function to compile and load deckfiles

```kotlin
val compiler = deckFileCompiler {
    fileName = file.name.removeSuffix(".deckfile")
    filePath = file.absolutePath
    referencesDirectory = "/home/user/IdeaProjects/DevDeck/references"
    taskBatchSize = 5
}

val deckfile = compiler.loadDeckFile()
deckfile.execute() // All tasks are run in parallel
```


### Currently supported tasks
* [CloneTask](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/CloneTask.kt): Clones a repository
* [InstallTask](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/InstallTask.kt): Installs a binary
* [RunTask](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/RunTask.kt): Runs a command in a shell
* [StepTask](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/StepTask.kt): Runs a group of tasks sequentially
* [ReferenceTask](src/desktopMain/kotlin/dev/justincodinguk/devdeck/core/deck_api/task/ReferenceTask.kt): Downloads installation references

### Documentation Status
Module is fully documented