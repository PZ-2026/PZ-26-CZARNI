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
import com.example.magazyn.api.dtos.UzytkownikDTO
import ui.screens.klient.ProfilTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZaopatrzeniowiecDashboard(onLogut: () -> Unit) {
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
            // Wewnątrz ZaopatrzeniowiecDashboard, w sekcji 'when'
            when (selectedItem.value) {
                0 -> {
                    // Tworzymy "sztuczne" DTO tylko po to, żeby typy się zgadzały
                    val fakeUser = UzytkownikDTO(
                        id = 4,
                        imie = "Ładowanie",
                        nazwisko = "...",
                        email = "",
                        telefon = "",
                        rola = 2
                    )
                    ProfilTab(fakeUser)
                }
                1 -> MagazynTab()
                2 -> DostawcyTab()
                3 -> HistoriaTab(4) // Tu historia już przyjmuje Int, więc jest OK
            }
        }
    }
}