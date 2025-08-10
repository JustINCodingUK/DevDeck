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

    override fun runDeckFile(deckFileModel: DeckFileModel, batchSize: Int) = flow {
        val compiler = inMemoryDeckFileCompiler {
            fileContents = deckFileModel.contents
            referencesDirectory = "/home/justinw/IdeaProjects/DevDeck/references"
            taskBatchSize = batchSize
        }
        val deckFile = compiler.loadDeckFile()
        try {
            deckFile.execute()
            emit(Result.success(Unit))
        } catch (exception: DeckFileSyntaxException) {
            emit(Result.failure(exception))
        }
    }
}