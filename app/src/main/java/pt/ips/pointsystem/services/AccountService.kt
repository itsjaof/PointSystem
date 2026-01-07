@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection"
)

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

/**
 * Serviço responsável pela gestão de autenticação e sessões de utilizador com o Appwrite.
 *
 * Esta classe encapsula as chamadas à API do Appwrite, abstraindo a complexidade de:
 * * Gestão de threads (força a execução em [Dispatchers.IO]).
 * * Tratamento de erros básicos (captura [AppwriteException] e regista logs).
 * * Fluxos de login (Email/Password e OAuth) e logout.
 *
 * @property account A instância do serviço [Account] do SDK do Appwrite inicializada com o cliente da aplicação.
 */
@Suppress("SpellCheckingInspection")
class AccountService(client: Client) {
    private val account = Account(client)

    /**
     * Verifica se existe uma sessão ativa e obtém os dados do utilizador atual.
     * * Esta função executa uma operação de I/O assíncrona para recuperar a conta.
     * Em caso de erro (ex: sem internet ou sem sessão), a exceção é capturada e logada.
     *
     * @return O objeto [User] com os dados do utilizador se a sessão for válida, ou `null` caso ocorra um erro.
     */
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

    /**
     * Autentica um utilizador utilizando email e palavra-passe.
     * * Cria uma nova sessão e, em caso de sucesso, recupera imediatamente os dados do utilizador.
     *
     * @param email O endereço de email do utilizador.
     * @param password A palavra-passe do utilizador.
     * @return O objeto [User] autenticado se o login for bem-sucedido, ou `null` se falhar.
     */
    suspend fun login(email: String, password: String) : User<Map<String, Any>>? {
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

    /**
     * Inicia o fluxo de autenticação OAuth2 utilizando a Google como fornecedor.
     * * Esta função necessita da Activity para lançar a interface de autenticação (geralmente um navegador).
     *
     * @param activity A [ComponentActivity] atual, necessária para instanciar o contexto do OAuth.
     * @return Retorna `Unit?` (null) se ocorrer uma exceção durante o processo.
     */
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

    /**
     * Encerra a sessão do utilizador atual.
     * * Remove as sessões ativas (faz logout) no servidor da Appwrite.
     * Não retorna valor, mas regista um erro no Log se a operação falhar.
     */
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