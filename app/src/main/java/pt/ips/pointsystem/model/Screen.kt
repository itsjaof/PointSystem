package pt.ips.pointsystem.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Ponto : Screen("ponto", "Ponto", Icons.Default.Schedule)
    object Historico : Screen("historico", "Histórico", Icons.Default.Description)
    object Perfil : Screen("perfil", "Perfil", Icons.Default.Person)
}