package ui.screens.klient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.utils.getRolaName
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.models.KlientViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfilTab(user: UzytkownikDTO?, klientViewModel: KlientViewModel = viewModel<KlientViewModel>()) {
    LaunchedEffect(user) {
        klientViewModel.setup(user)
    }

    // Stan przewijania
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter // Centrowanie na tabletach
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // DODAJEMY PRZEWIJANIE:
                .verticalScroll(scrollState)
                // OGRANICZAMY SZEROKOŚĆ DLA TABLETÓW:
                .widthIn(max = 450.dp)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Sekcja Headera
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (klientViewModel.isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = klientViewModel.imie,
                        onValueChange = { klientViewModel.imie = it },
                        label = { Text("Imię") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = klientViewModel.nazwisko,
                        onValueChange = { klientViewModel.nazwisko = it },
                        label = { Text("Nazwisko") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
            else {
                Text(
                    text = "${klientViewModel.imie} ${klientViewModel.nazwisko}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            SuggestionChip(
                onClick = { },
                label = { Text(getRolaName(user?.rola)) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Karta z danymi
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (klientViewModel.isEditing) "Edytuj dane" else "Dane kontaktowe",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    ImprovedInfoRow(Icons.Default.Email, "Email", klientViewModel.email, klientViewModel.isEditing, {klientViewModel.email = it})
                    HorizontalDivider(Modifier.padding(vertical = 8.dp),0.5.dp)
                    ImprovedInfoRow(Icons.Default.Phone, "Telefon", klientViewModel.telefon, klientViewModel.isEditing, {klientViewModel.telefon = it})
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), 0.5.dp)
                    ImprovedInfoRow(Icons.Default.Business, "Firma",klientViewModel.firma, klientViewModel.isEditing, onValueChange = {klientViewModel.firma = it})
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), 0.5.dp)
                    ImprovedInfoRow(Icons.Default.CreditCard,"NIP", klientViewModel.nip, klientViewModel.isEditing, {klientViewModel.nip = it})
                }
            }

            // Zamiast weight(1f) dajemy elastyczny odstęp:
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    klientViewModel.onButtonClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (klientViewModel.isEditing) "Zapisz" else "Edytuj dane profilowe")
            }

            // Dodatkowy margines na dole, żeby przycisk nie dotykał krawędzi po przewinięciu
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ImprovedInfoRow(icon: ImageVector, label: String, value: String, isEditing: Boolean = false, onValueChange: (String) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            if (isEditing) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(4.dp)
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}