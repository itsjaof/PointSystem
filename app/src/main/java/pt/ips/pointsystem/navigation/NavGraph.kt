package pt.ips.pointsystem.navigation

import PerfilScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.ips.pointsystem.model.Screen
import pt.ips.pointsystem.ui.HistoricoScreen
import pt.ips.pointsystem.ui.HomeScreen
import pt.ips.pointsystem.ui.LoginScreen
import pt.ips.pointsystem.ui.PontoScreen

@Composable
fun NavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = Modifier.padding(padding)
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Ponto.route) { PontoScreen() }
        composable(Screen.Historico.route) { HistoricoScreen() }
        composable(Screen.Perfil.route) { PerfilScreen() }
    }
}