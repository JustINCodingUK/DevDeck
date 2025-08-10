package dev.justincodinguk.devdeck.core.network.firestore

interface FirestoreService<T> {

    suspend fun createDocument(documentObject: T): Result<Unit>

    suspend fun updateDocument(documentObject: T): Result<Unit>

    suspend fun getDocument(id: String): Result<T>

    suspend fun deleteDocument(id: String): Result<Unit>

}