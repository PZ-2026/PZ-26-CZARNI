package ui.screens.zaopatrzeniowiec

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.example.magazyn.api.models.HistoriaViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SzczegolyZamowieniaScreen(
    zamowienie: HistoriaZamowienia,
    onNavigateBack: () -> Unit,
    viewModel: HistoriaViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zamówienie #${zamowienie.id}") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Dostawca: ${zamowienie.nazwaDostawcy}", style = MaterialTheme.typography.titleLarge)
            Text("Data: ${zamowienie.formatowanaData}", style = MaterialTheme.typography.bodyMedium)
            Text("Ilość produktów: ${zamowienie.sumaProduktow}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Przycisk zmiany statusu
            if (zamowienie.status != 2) {
                Button(
                    onClick = {
                        // Wywołujemy funkcję w widoku, która połączy się z backendem
                        viewModel.zmienStatusZamowienia(zamowienie.id, 2, 4)
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Oznacz jako Zrealizowane")
                }
            } else {
                Text(
                    text = "Zamówienie zostało już zrealizowane.",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}