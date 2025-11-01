package pt.ips.pointsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import pt.ips.pointsystem.navigation.NavGraph
import pt.ips.pointsystem.navigation.Navigation
import pt.ips.pointsystem.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppPreview() {
    val fakeNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            Navigation(fakeNavController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            HomeScreen()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            Navigation(navController)
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            padding = innerPadding
        )
    }
}