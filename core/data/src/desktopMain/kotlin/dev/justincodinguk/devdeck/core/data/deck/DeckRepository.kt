package dev.justincodinguk.devdeck.core.data.deck

import dev.justincodinguk.devdeck.core.deck_api.task.TaskHandler
import dev.justincodinguk.devdeck.core.model.DeckFileModel
import kotlinx.coroutines.flow.Flow

interface DeckRepository {

    fun loadDeckFileFromNetwork(id: String): Flow<Result<DeckFileModel>>

    fun searchDeck(name: String): Flow<Result<List<DeckFileModel>>>

    fun scanFilesystem(): Flow<Result<String>>

    fun runDeckFile(
        deckFileModel: DeckFileModel,
        refDir: String,
        batchSize: Int
    ): Flow<Result<TaskHandler>>

}