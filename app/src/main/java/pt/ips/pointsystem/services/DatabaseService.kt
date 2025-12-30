package pt.ips.pointsystem.services

import android.util.Log
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
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
}