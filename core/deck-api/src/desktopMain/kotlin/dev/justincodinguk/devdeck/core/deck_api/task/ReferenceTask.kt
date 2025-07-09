package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReferenceLoader

class ReferenceTask(
    private val url: String,
    private val installReferenceLoader: InstallReferenceLoader
) : Task {
    override val name = "Resolve installation references"

    override suspend fun execute() = installReferenceLoader.loadNetworkReference(url)
}