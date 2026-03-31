package ui.screens.magazynier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class MagazynierNavItem(val title: String, val icon: ImageVector) {
    object Zlecenia : MagazynierNavItem("Zlecenia", Icons.Default.Assignment)
    object Stan : MagazynierNavItem("Stan", Icons.Default.Inventory)
    object Profil : MagazynierNavItem("Profil", Icons.Default.Person)
    object Wyloguj : MagazynierNavItem("Wyloguj", Icons.AutoMirrored.Filled.ExitToApp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagazynierDashboard(onLogout: () -> Unit) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        MagazynierNavItem.Zlecenia,
        MagazynierNavItem.Stan,
        MagazynierNavItem.Profil,
        MagazynierNavItem.Wyloguj
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
                    Column {
                        Text(
                            text = "Panel Magazyniera",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
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
                        selected = selectedItem == index,
                        onClick = {
                            if (item is MagazynierNavItem.Wyloguj) {
                                onLogout()
                            } else {
                                selectedItem = index
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem) {
                0 -> ZleceniaTab()
                1 -> StanMagazynowyTab()
                2 -> ProfilMagazynieraTab()
            }
        }
    }
}
