package dev.justincodinguk.devdeck.core.data.deck

import dev.justincodinguk.devdeck.core.model.DeckFileModel
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

    fun loadDeckFileFromNetwork(id: String): Flow<Result<DeckFileModel>>

    fun scanFilesystem(): Flow<Result<String>>

    fun runDeckFile(deckFileModel: DeckFileModel, batchSize: Int): Flow<Result<Unit>>

}