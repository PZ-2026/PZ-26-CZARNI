package ui.screens.klient

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Profil : BottomNavItem("Profil", Icons.Default.Person)
    object Zamow : BottomNavItem("Zamów", Icons.Default.ShoppingBag)
    object Historia : BottomNavItem("Historia", Icons.Default.History)
    object Wyloguj : BottomNavItem("Wyloguj", Icons.AutoMirrored.Filled.Logout)
}