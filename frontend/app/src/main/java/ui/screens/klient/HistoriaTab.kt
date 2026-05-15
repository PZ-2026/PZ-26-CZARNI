package ui.screens.klient

import android.content.Intent
import android.widget.Toast
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.HistoriaZamowieniaDTO
import com.example.magazyn.api.models.HistoriaViewModel
import com.example.magazyn.utils.StatusBadge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL


@Composable
fun HistoriaTab(id: Int, historiaViewModel: HistoriaViewModel = viewModel<HistoriaViewModel>()) {
    // Stan dla okna szczegółów
    var wybraneZamowienie by remember { mutableStateOf<HistoriaZamowieniaDTO?>(null) }

    LaunchedEffect(id) {
        historiaViewModel.fetchHistoriaKlient(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Twoje zamówienia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(historiaViewModel.historiaList) { zamowienie ->
                // Przekazujemy funkcję otwierającą dialog
                HistoryItem(zamowienie, onShowDetails = { wybraneZamowienie = zamowienie })
            }
        }
    }

    // Wyświetlanie dialogu szczegółów
    wybraneZamowienie?.let { zamowienie ->
        SzczegolyZamowieniaDialog(
            zamowienie = zamowienie,
            onDismiss = { wybraneZamowienie = null }
        )
    }
}

@Composable
fun HistoryItem(zamowienie: HistoriaZamowieniaDTO, onShowDetails: () -> Unit) {
    val accentColor = when (zamowienie.status) {
        0 -> Color(0xFFB45309)
        1 -> Color(0xFF047857)
        2 -> Color(0xFF1D4ED8)
        else -> Color(0xFF6B7280)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onShowDetails
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Inventory2, null, modifier = Modifier.size(18.dp), tint = accentColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Zamówienie #${zamowienie.id}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    StatusBadge(zamowienie.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(zamowienie.data, fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF5F5F5))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Produkty: ${zamowienie.sumaProduktow} szt.", fontSize = 13.sp, color = Color.Gray)
                    Text("${String.format("%.2f", zamowienie.kwota)} zł", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                }
            }
        }
    }
}

@Composable
fun SzczegolyZamowieniaDialog(
    zamowienie: HistoriaZamowieniaDTO,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zamówienie #${zamowienie.id}", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Status: ${when(zamowienie.status) {
                    0 -> "Oczekujące"
                    1 -> "W realizacji"
                    2 -> "Zrealizowane"
                    else -> "Nieznany"
                }}", fontSize = 14.sp)
                Text("Data złożenia: ${zamowienie.data}", fontSize = 14.sp)
                Text("Łączna kwota: ${String.format("%.2f", zamowienie.kwota)} zł", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Produkty: ${zamowienie.sumaProduktow} szt.", fontSize = 14.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        try {
                            val pdfUrl = "${RetrofitInstance.BASE_URL}api/raporty/pobierz/${zamowienie.id}"

                            val url = URL(pdfUrl)
                            val connection = url.openConnection() as HttpURLConnection
                            connection.connectTimeout = 5000

                            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                                val bytes = connection.inputStream.readBytes()

                                // Zapis do publicznego folderu Pobrane
                                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                val file = File(downloadDir, "Faktura_${zamowienie.id}.pdf")
                                file.writeBytes(bytes)

                                // Powiadomienie systemu o nowym pliku
                                android.media.MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)

                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Pobrano do folderu Pobrane", Toast.LENGTH_LONG).show()

                                    try {
                                        val uri = androidx.core.content.FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "application/pdf")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }

                                        // To dodatkowo upewnia się, że system nada uprawnienia dla URI
                                        context.grantUriPermission(context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Log.e("PDF_OPEN", "Nie można otworzyć PDF: ${e.message}")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("PDF_ERROR", "Błąd: ${e.message}")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037))
            ) {
                Icon(Icons.Default.PictureAsPdf, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Pobierz PDF")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Zamknij") }
        }
    )
}