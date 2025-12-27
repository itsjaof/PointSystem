package pt.ips.pointsystem.ui

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun NfcScanScreen(
    navController: NavController,
    nfcCode: String?,
    onCodeConsumed: () -> Unit
) {
    LaunchedEffect(nfcCode) {
        if (!nfcCode.isNullOrEmpty()) {
            Log.d("NfcScanScreen", "Código NFC: $nfcCode")
            navController.popBackStack()
            onCodeConsumed()
        }
    }

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
}