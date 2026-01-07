package pt.ips.pointsystem.services

import android.util.Log
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.Document
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseService {
    private val databases = Databases(AppWriteClient.client)
    private val databaseId = "pointsystem"

    suspend fun store(collectionId: String, data: Any): Boolean {
        return withContext(Dispatchers.IO) {
             try {
                databases.createDocument(
                    databaseId,
                    collectionId,
                    documentId = ID.unique(),
                    data = data
                )
                true
            } catch (ex: AppwriteException) {
                Log.e("DatabaseService", "Erro ao registar na base de dados\n" +
                        "ID: $databaseId\n" +
                        "Collection: $collectionId\n" +
                        "Data: $data", ex)
                false
            }
        }
    }

    suspend fun listDocuments(collectionId: String, queries: List<String>? = null): List<Document<Map<String, Any>>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = databases.listDocuments(
                    databaseId,
                    collectionId,
                    queries
                )
                response.documents
            } catch (ex: AppwriteException) {
                Log.e("DatabaseService", "Erro ao listar documentos", ex)
                emptyList()
            }
        }
    }

    suspend fun getLastPicagem(collectionId: String, userId: String): Document<Map<String, Any>>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = listDocuments(collectionId,
                    queries = listOf(
                        Query.equal("userId", userId),
                        Query.orderDesc("timestamp"),
                        Query.limit(1)
                    )
                )

                response.firstOrNull()
            } catch (ex: AppwriteException) {
                Log.e("DatabaseService", "Erro ao obter ultima picagem", ex)
                null
            }
        }
    }
}