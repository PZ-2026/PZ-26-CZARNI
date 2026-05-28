package ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminBottomNavItem(val title: String, val icon: ImageVector) {
    object Uzytkownicy : AdminBottomNavItem("Konta", Icons.Default.People)
    object Dostawcy : AdminBottomNavItem("Dostawcy", Icons.Default.LocalShipping)
    object Magazyn : AdminBottomNavItem("Magazyn", Icons.Default.Warehouse)
    object Finanse : AdminBottomNavItem("Finanse", Icons.Default.Payments)
    object Raporty : AdminBottomNavItem("Raporty", Icons.AutoMirrored.Filled.Assignment)
}
