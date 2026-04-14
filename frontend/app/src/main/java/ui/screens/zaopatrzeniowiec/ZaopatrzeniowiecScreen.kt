package ui.screens.zaopatrzeniowiec

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZaopatrzeniowiecDashboard(onLogut: () -> Unit) {
    // Stan aktywnego przycisku (później zastąpisz to NavHostem)
    val selectedItem = remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavItem.Profil,
        BottomNavItem.Magazyn,
        BottomNavItem.Dostawcy,
        BottomNavItem.Historia,
        BottomNavItem.Wyloguj
    )

    Scaffold(
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
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        selected = selectedItem.value == index,
                        onClick = {
                            selectedItem.value = index
                            if (item is BottomNavItem.Wyloguj) {
                                onLogut()
                            }
                        }
                    )
                }
            }
        }
    ) { it -> // padding z Scaffold
        Box(modifier = Modifier.padding(it)) {
            when (selectedItem.value) {
                0 -> ProfilTab()
                1 -> MagazynTab()
                2 -> DostawcyTab()
                3 -> HistoriaTab()
            }
        }
    }
}