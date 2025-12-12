package pt.ips.pointsystem.services

import android.util.Log
import androidx.activity.ComponentActivity
import io.appwrite.Client
import io.appwrite.enums.OAuthProvider
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountService(client: Client) {
    private val account = Account(client)

    suspend fun getLoggedIn() : User<Map<String, Any>>? {
        return withContext(Dispatchers.IO) {
            try {
                account.get()
            } catch (ex: AppwriteException) {
                Log.e("AccountService", "Erro: ${ex.message}")
                null
            }
        }
    }

    suspend fun login(email: String, password: String) : User<Map<String, Any>>? {
        // withContext(Dispatchers.IO)
        // Isto muda o contexto de execução de uma corrotina de uma thread para outra.
        // O Dispatchers.IO faz com que mude para uma thread otimizada para operações de IO (ficheiros, rede, etc).
        // Assim que o bloco de código termina, muda para a thread em que estava

        return withContext(Dispatchers.IO) {
            try {
                account.createEmailPasswordSession(email, password)

                getLoggedIn()
            } catch (ex: AppwriteException) {
                Log.e("AccountService", "Erro: ${ex.message}")
                null
            }
        }
    }

    suspend fun loginWithGoogle(activity: ComponentActivity): Unit? {
        return withContext(Dispatchers.IO) {
            try {
                account.createOAuth2Session(
                    activity,
                    provider = OAuthProvider.GOOGLE
                )
            } catch (ex: AppwriteException) {
                Log.e("AccountService", "Erro: ${ex.message}")
                null
            } catch (ex: IllegalStateException) {
                Log.e("AccountService", "O processo de login foi interrompido: ${ex.message}")
                null
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                account.deleteSessions()
            } catch (ex: AppwriteException) {
                Log.e("AccountService", "Erro: ${ex.message}")
            }
        }
    }
}