package pt.ips.pointsystem.model

import android.util.Log
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.services.DatabaseService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class Picagem(
    val userId: String,
    val nfcId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String? = null,
    val type: PicagemType = PicagemType.ENTRADA
) {
    var collectionId: String = "692f2e1a002875f2f416"

    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "nfcId" to nfcId,
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to timestamp.toString(),
            "type" to type.toString()
        )
    }

    suspend fun registerPunch(type: PicagemType, nfcId: String, latitude: Double, longitude: Double) {
        val account = AccountService(AppWriteClient.client).getLoggedIn()

        try {
            if (account != null) {
                Log.d("AccountService", "USER ID: ${account.id}")

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                sdf.timeZone = TimeZone.getTimeZone("UTC")
                val nowStr = sdf.format(Date())

                val data = Picagem(
                    account.id,
                    nfcId,
                    latitude,
                    longitude,
                    nowStr,
                    type
                )

                DatabaseService().store(
                    collectionId,
                    data.toMap()
                )
            } else {
                Log.e("RegisterPunch", "O utilizado não tem sessão.")
            }
        } catch (ex: Exception) {
            Log.e("RegisterPunch", "Erro ao obt er o utilizador ou registar a picagem", ex)
        }
    }
}