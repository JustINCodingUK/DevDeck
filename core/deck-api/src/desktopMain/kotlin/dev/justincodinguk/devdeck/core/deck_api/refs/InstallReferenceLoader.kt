package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.InstallReferenceError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import java.io.File

class InstallReferenceLoader(
    private val directory: String,
    private val httpClient: HttpClient,
    private val jsonSerializer: Json
) {

    private val networkRefs = mutableMapOf<String, InstallReference>()

    private fun loadReference(id: String) : InstallReference? {
        try {
            println("Loading $directory/${id.replace('.','/')}.json")
            val rawJson = File("$directory/${id.replace('.','/')}.json").readText()
            return jsonSerializer.decodeFromString(rawJson)
        } catch (e: Exception) {
            throw e
            return null
        }
    }

    suspend fun loadNetworkReference(url: String) {
        try {
            val installReference = httpClient.get(url).body<InstallReference>()
            networkRefs[installReference.id] = installReference
        } catch (e: Exception) {
            throw InstallReferenceError(url)
        }
    }

    fun getReference(id: String) : InstallReference {
        return loadReference(id) ?: networkRefs[id] ?: throw InstallReferenceError(id)
    }

}