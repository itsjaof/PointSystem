package pt.ips.pointsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pt.ips.pointsystem.navigation.NavGraph
import pt.ips.pointsystem.navigation.Navigation
import pt.ips.pointsystem.services.NfcService
import pt.ips.pointsystem.ui.HomeScreen

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
                onClearCode = { scannedTagId = null } // Função para limpar o código
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
}

@Composable
fun App(nfcCode: String? = null, onClearCode: () -> Unit = {}) {
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
            onCodeConsumed = onClearCode
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