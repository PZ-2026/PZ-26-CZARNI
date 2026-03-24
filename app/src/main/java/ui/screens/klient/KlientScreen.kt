package ui.screens.klient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun KlientDashboard() {
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf(
        BottomNavItem.Profil,
        BottomNavItem.Zamow,
        BottomNavItem.Historia,
        BottomNavItem.Wyloguj
    )

    Scaffold(
        //containerColor = Color(0xFFFCF7F7), // To jasne tło z obrazka
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                // Teraz automatycznie pobierze nasz MutedRose
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 6.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary, // Bordowy
                        modifier = Modifier.size(28.dp)
                    )
                    // ... reszta kolumny z tekstem ...
                }
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.intValue == index,
                        onClick = { selectedItem.intValue = index },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF5D4037),
                            indicatorColor = Color(0xFFF2EAE8)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem.intValue) {
                0 -> ProfilTab() // Tutaj wywołujemy Twój wygląd z obrazka
                1 -> Text("Widok Zamówień")
                2 -> Text("Widok Historii")
                3 -> Text("Logika Wylogowania")
            }
        }
    }
}