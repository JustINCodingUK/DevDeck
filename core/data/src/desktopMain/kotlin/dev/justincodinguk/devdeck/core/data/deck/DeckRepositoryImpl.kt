package dev.justincodinguk.devdeck.core.data.deck

import dev.justincodinguk.devdeck.core.deck_api.DeckFileSyntaxException
import dev.justincodinguk.devdeck.core.deck_api.inMemoryDeckFileCompiler
import dev.justincodinguk.devdeck.core.model.DeckFileModel
import dev.justincodinguk.devdeck.core.network.firestore.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class DeckRepositoryImpl(
    private val deckFileFirestoreService: FirestoreService<DeckFileModel>
) : DeckRepository {

    override fun loadDeckFileFromNetwork(id: String) = flow {
        val deckFile = deckFileFirestoreService.getDocument(id)
        emit(deckFile)
    }

    override fun scanFilesystem(): Flow<Result<String>> {
        TODO("Not yet implemented")
    }

    override fun runDeckFile(
        deckFileModel: DeckFileModel,
        refDir: String,
        batchSize: Int
    ) = flow {
        val compiler = inMemoryDeckFileCompiler {
            fileContents = deckFileModel.contents
            referencesDirectory = refDir
            taskBatchSize = batchSize
        }
        val deckFile = compiler.loadDeckFile()
        try {
            deckFile.execute()
            emit(Result.success(deckFile.taskHandler))
        } catch (exception: DeckFileSyntaxException) {
            emit(Result.failure(exception))
        }
    }

    override fun searchDeck(name: String): Flow<Result<List<DeckFileModel>>> {
        TODO("Not yet implemented")
    }
}