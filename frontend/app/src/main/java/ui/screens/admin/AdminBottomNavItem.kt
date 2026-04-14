package ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminBottomNavItem(val title: String, val icon: ImageVector) {
    object Uzytkownicy : AdminBottomNavItem("Użytkownicy", Icons.Default.People)
    object Finanse : AdminBottomNavItem("Finanse", Icons.Default.Payments)
    object Raporty : AdminBottomNavItem("Raporty", Icons.AutoMirrored.Filled.Assignment)
    object Ustawienia : AdminBottomNavItem("Ustawienia", Icons.Default.Settings)
    object Wyloguj : AdminBottomNavItem("Wyloguj", Icons.AutoMirrored.Filled.ExitToApp)
}