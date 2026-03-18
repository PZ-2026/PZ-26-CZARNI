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
fun ZaopatrzeniowiecDashboard() {
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
            // Używamy Surface zamiast zwykłego TopAppBar dla lepszej kontroli nad cieniem i kształtem
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f), // Subtelna przezroczystość
                tonalElevation = 6.dp, // Dodaje delikatny cień pod spodem
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp) // Zaokrąglone dolne rogi
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding() // Zapewnia, że tekst nie wejdzie pod ikonki systemowe (zegar, bateria)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikona obok tytułu dodaje profesjonalizmu
                    Icon(
                        imageVector = Icons.Default.Dashboard, // Musisz dodać ten import
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Panel Zaopatrzeniowca",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold, // Pogrubienie dla nowoczesnego wyglądu
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Witaj ponownie!",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
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
                                // TODO: Logika wylogowania (czyszczenie tokena JWT) [cite: 56]
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