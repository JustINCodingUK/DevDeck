package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReferenceLoader
import org.slf4j.LoggerFactory

/**
 * Implementation of [Task] to resolve installation references from a remote location.
 *
 * @param url The URL to fetch installation references from
 * @param installReferenceLoader The [InstallReferenceLoader] to use for loading references
 */
class ReferenceTask private constructor(
    private val url: String,
    private val installReferenceLoader: InstallReferenceLoader
) : Task {
    override val name = "Resolve installation references"

    private val logger = LoggerFactory.getLogger("ReferenceTask")
    /**
     * Executes the [ReferenceTask]. Fetches installation references from the specified URL.
     */
    override suspend fun execute() {
        logger.info(name)
        installReferenceLoader.loadNetworkReference(url)
    }

    /**
     * Builder class for [ReferenceTask].
     */
    class Builder : Task.Builder<ReferenceTask> {
        lateinit var url: String
        lateinit var installReferenceLoader: InstallReferenceLoader

        override fun build() = ReferenceTask(url, installReferenceLoader)
    }
}