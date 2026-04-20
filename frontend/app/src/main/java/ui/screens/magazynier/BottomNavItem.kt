package ui.screens.magazynier

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Profil : BottomNavItem("Profil", Icons.Default.Person)
    object Zamowienia : BottomNavItem("Zlecenia", Icons.Default.ListAlt)
    object Skaner : BottomNavItem("Skaner", Icons.Default.QrCodeScanner)
    object Magazyn : BottomNavItem("Magazyn", Icons.Default.Inventory)
    object Wyloguj : BottomNavItem("Wyloguj", Icons.AutoMirrored.Filled.ExitToApp)
}
