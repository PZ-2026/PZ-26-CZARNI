package ui.screens.magazynier

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Profil : BottomNavItem("Profil", Icons.Default.Person)
    object Magazyn : BottomNavItem("Magazyn", Icons.Default.Storage)
    object Historia : BottomNavItem("Historia", Icons.Default.History)
    object Wyloguj : BottomNavItem("Wyloguj", Icons.AutoMirrored.Filled.ExitToApp)
}
