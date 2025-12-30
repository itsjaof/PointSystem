package pt.ips.pointsystem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.ips.pointsystem.model.Picagem
import pt.ips.pointsystem.navigation.NavGraph
import pt.ips.pointsystem.navigation.Navigation
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.services.DatabaseService
import pt.ips.pointsystem.services.NfcService
import pt.ips.pointsystem.ui.HomeScreen
import java.time.Instant

class MainActivity : ComponentActivity() {
    private lateinit var nfcService: NfcService
    private var scannedTagId by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcService = NfcService(this) { tagId ->
            scannedTagId = tagId
        }

        enableEdgeToEdge()
        setContent {
            App(
                nfcCode = scannedTagId,
                onClearCode = { scannedTagId = null },
                onRegistarPonto = { idCartao, lat, long ->
                    registerPunch(idCartao, lat, long)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        nfcService.enableReader()
    }

    override fun onPause() {
        super.onPause()
        nfcService.disableReader()
    }

    private fun registerPunch(nfcId: String, latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            val account = AccountService(AppWriteClient.client).getLoggedIn()

            try {
                if (account != null) {
                    Log.d("AccountService", "USER ID: ${account.id}")

                    val data = Picagem(
                        account.id,
                        nfcId,
                        latitude,
                        longitude,
                        Instant.now().toString()
                    )

                    DatabaseService().store(
                        data.collectionId,
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
}

@Composable
fun App(nfcCode: String? = null, onClearCode: () -> Unit = {}, onRegistarPonto: (String, Double, Double) -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val noNavRoutes = listOf("login", "nfcScan")

            if (currentRoute !in noNavRoutes) Navigation(navController)
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            padding = innerPadding,
            nfcCode = nfcCode,
            onCodeConsumed = onClearCode,
            onPicagemValia = onRegistarPonto
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    val fakeNavController = rememberNavController()
    Scaffold(bottomBar = { Navigation(fakeNavController) }) {
        Box(modifier = Modifier.padding(it)) { HomeScreen() }
    }
}