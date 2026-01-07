package pt.ips.pointsystem.services

import android.content.Context
import io.appwrite.Client

/**
 * Singleton responsável por gerir a instância global do cliente Appwrite.
 *
 * Este objeto atua como o ponto central de configuração da ligação ao servidor Appwrite,
 * garantindo que existe apenas uma instância do [Client] inicializada durante o ciclo
 * de vida da aplicação.
 *
 * @property [client] A instância nativa do cliente Appwrite.
 * @property [account] A instância do serviço de contas [AccountService].
 */
object AppWriteClient {
    lateinit var client: Client
    internal lateinit var account: AccountService

    /**
     * Obtém a instância do Cliente Appwrite, inicializando-a se necessário.
     *
     * 1. Verifica se o [client] já existe. Se não, cria-o com as configurações do servidor.
     * 2. Verifica se o serviço [account] já existe. Se não, instancia-o com o cliente criado.
     *
     * @param context O [Context] do Android (Application ou Activity context), necessário para inicializar o SDK.
     * @return A instância configurada de [Client] pronta a utilizar.
     */
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