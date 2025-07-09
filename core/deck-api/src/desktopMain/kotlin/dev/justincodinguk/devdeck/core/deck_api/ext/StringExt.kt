package dev.justincodinguk.devdeck.core.deck_api.ext

import java.io.File

fun String.isUnsafe() = any {
    !it.isLetterOrDigit() && it !in listOf('-', '_', '.', " ", "@")
}

fun String.runAsCommand(
    workingDir: File = File("."),
    useLoginShell: Boolean = true
): Int {
    val fullCommand = if (useLoginShell) {
        listOf("bash", "-lc", this)
    } else {
        listOf("sh", "-c", this)
    }

    return ProcessBuilder(fullCommand)
        .directory(workingDir)
        .inheritIO()
        .start()
        .waitFor()
}