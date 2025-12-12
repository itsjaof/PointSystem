package pt.ips.pointsystem.services

import android.content.Context
import io.appwrite.Client

object AppWriteClient {
    lateinit var client: Client
    internal lateinit var account: AccountService

    fun getInstance(context: Context) : Client {
        if (!::client.isInitialized) {
            client = Client(context)
                .setEndpoint("https://appwrite.hugetower.cloud/v1")
                .setProject("691e40c3003a9ce423c0")
                .setSelfSigned(true)
        }

        if (!::account.isInitialized) {
            account = AccountService(client)
        }

        return client
    }
}