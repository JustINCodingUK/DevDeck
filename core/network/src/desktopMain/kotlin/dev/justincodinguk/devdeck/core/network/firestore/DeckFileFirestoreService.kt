package dev.justincodinguk.devdeck.core.network.firestore

import DevDeck.core.network.BuildConfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.justincodinguk.devdeck.core.model.DeckFileModel

internal class DeckFileFirestoreService : FirestoreService<DeckFileModel> {

    private val firestore = Firebase.firestore

    init {
        if(BuildConfig.DEBUG) {
            firestore.useEmulator("127.0.0.1", 8000)
        }
    }

    override suspend fun createDocument(documentObject: DeckFileModel) : Result<Unit> {
        try {
            firestore
                .collection("deckfiles")
                .add(documentObject)
                return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun updateDocument(documentObject: DeckFileModel): Result<Unit> {
        try {
            firestore
                .collection("deckfiles")
                .document(documentObject.id)
                .set(documentObject)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getDocument(id: String): Result<DeckFileModel> {
        try {
            val deckFileModel = firestore
                .collection("deckfiles")
                .document(id)
                .get()
                .data<DeckFileModel>()
            return Result.success(deckFileModel)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun deleteDocument(id: String): Result<Unit> {
        try {
            firestore
                .collection("deckfiles")
                .document(id)
                .delete()
                return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}