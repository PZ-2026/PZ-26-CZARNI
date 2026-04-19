package ui.screens.magazynier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magazyn.api.dtos.UzytkownikDTO
import ui.screens.zaopatrzeniowiec.ProfilTab // Zmieniono import na wersję z parametrem
import ui.screens.zaopatrzeniowiec.MagazynTab
import ui.screens.zaopatrzeniowiec.HistoriaTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagazynierDashboard(onLogout: () -> Unit) {
    val selectedItem = remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavItem.Profil,
        BottomNavItem.Magazyn,
        BottomNavItem.Historia,
        BottomNavItem.Wyloguj
    )

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
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
                                onLogout()
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem.value) {
                0 -> {
                    val fakeUser = UzytkownikDTO(
                        id = 0,
                        imie = "Magazynier",
                        nazwisko = "(Brak danych)",
                        email = "-",
                        telefon = "-",
                        rola = 3 // Rola Magazyniera
                    )
                    ProfilTab(fakeUser)
                }
                1 -> MagazynTab()
                2 -> HistoriaTab(0)
            }
        }
    }
}
