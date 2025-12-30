package pt.ips.pointsystem.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contactless
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.ips.pointsystem.services.LocationService

@Composable
fun NfcScanScreen(
    navController: NavController,
    nfcCode: String?,
    onCodeConsumed: () -> Unit,
    onPicagemValida: (String, Double, Double) -> Unit
) {
    val context = LocalContext.current
    var hasLocationPermission by remember{ mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> hasLocationPermission = isGranted }
    )

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(nfcCode) {
        if (!nfcCode.isNullOrEmpty() && hasLocationPermission) {
            Log.d("NfcScanScreen", "Código NFC: $nfcCode. A obter localização...")

            val locationService = LocationService(context)

            locationService.getCurrentLocation { location ->
                if (location != null) {
                    Log.d("NfcScanScreen", "Localização obtida: ${location.latitude}, ${location.longitude}")

                    onPicagemValida(nfcCode, location.latitude, location.longitude)
                    Toast.makeText(context, "Ponto registrado com sucesso!", Toast.LENGTH_SHORT).show()

                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Erro: Ligue o GPS e tente novamente", Toast.LENGTH_LONG).show()
                }
            }
        }

        onCodeConsumed()
    }

    if(hasLocationPermission) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone ou Animação
            Icon(
                imageVector = Icons.Default.Contactless, // Podes mudar para um ícone de NFC/Wifi
                contentDescription = "NFC",
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Aproxima o telemóvel à tag NFC",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "A aguardar leitura...", color = Color.Gray)

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancelar")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("É necessário permitir a localização para registar o ponto.", color = Color.Red)

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cancelar")
            }
        }
    }
}