package ui.screens.magazynier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.magazyn.api.dtos.UzytkownikDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagazynierDashboard(user: UzytkownikDTO?, onLogout: () -> Unit) {
    val selectedItem = remember { mutableStateOf(0) }
    val items = listOf(
        BottomNavItem.Profil,
        BottomNavItem.Zamowienia,
        BottomNavItem.Skaner,
        BottomNavItem.Magazyn,
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
                        imageVector = Icons.Default.Warehouse,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Panel Magazyniera",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
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
                            if (item is BottomNavItem.Wyloguj) {
                                onLogout()
                            } else {
                                selectedItem.value = index
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedItem.value) {
                0 -> ProfilTab(user)
                1 -> ZleceniaTab()
                2 -> SkanerTab()
                3 -> MagazynTab()
            }
        }
    }
}
