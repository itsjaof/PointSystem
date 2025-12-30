package pt.ips.pointsystem.model

data class Picagem(
    val userId: String,
    val nfcId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
) {
    var collectionId: String = "692f2e1a002875f2f416"

    fun toMap(): Map<String, Any> {
        return mapOf(
            "userId" to userId,
            "nfcId" to nfcId,
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to timestamp
        )
    }
}