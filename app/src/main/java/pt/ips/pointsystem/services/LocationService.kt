package pt.ips.pointsystem.services

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationService(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationFetched: (Location?) -> Unit) {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location: Location? ->
            onLocationFetched(location)
        }.addOnFailureListener { e ->
            onLocationFetched(null)
        }
    }
}