package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.MissingInstallReferenceException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Loads installation references from a directory or network.
 *
 * @param directory The directory to load references from
 * @param httpClient The [HttpClient] to use for network requests
 * @param jsonSerializer The [Json] serializer to use for parsing JSON
 */
class InstallReferenceLoader(
    private val directory: String,
    private val httpClient: HttpClient,
    private val jsonSerializer: Json
) {

    /** Network cache of references */
    private val networkRefs = mutableMapOf<String, InstallReference>()

    /**
     * Loads an installation reference from [directory]
     *
     * @param id The ID of the reference to load
     *
     * @return The loaded [InstallReference] or null if it doesn't exist
     */
    private fun loadReference(id: String) : InstallReference? {
        try {
            println("Loading $directory/${id.replace('.','/')}.json")
            val rawJson = File("$directory/${id.replace('.','/')}.json").readText()
            return jsonSerializer.decodeFromString(rawJson)
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * Loads an installation reference from the network and caches it.
     *
     * @param url The URL to load the reference from
     *
     * @throws MissingInstallReferenceException If the reference cannot be found
     */
    suspend fun loadNetworkReference(url: String) {
        try {
            val installReference = httpClient.get(url).body<InstallReference>()
            networkRefs[installReference.id] = installReference
        } catch (e: Exception) {
            throw MissingInstallReferenceException(url)
        }
    }

    /**
     * Gets an installation reference by ID.
     *
     * @param id The ID of the reference to get
     *
     * @return The [InstallReference] with the specified ID
     *
     * @throws MissingInstallReferenceException If the reference cannot be found in either local storage or the network cache
     */
    fun getReference(id: String) : InstallReference {
        return loadReference(id) ?: networkRefs[id] ?: throw MissingInstallReferenceException(id)
    }

}