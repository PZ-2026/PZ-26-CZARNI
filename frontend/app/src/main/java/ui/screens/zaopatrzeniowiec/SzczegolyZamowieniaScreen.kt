package ui.screens.zaopatrzeniowiec

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.models.HistoriaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SzczegolyZamowieniaScreen(
    zamowienie: HistoriaZamowienia,
    onNavigateBack: () -> Unit,
    viewModel: HistoriaViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isDownloading by remember { mutableStateOf(false) }

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- SEKACJA INFORMACYJNA ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dostawca: ${zamowienie.nazwaDostawcy}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Data: ${zamowienie.formatowanaData}", style = MaterialTheme.typography.bodyMedium)
                    Text("Ilość produktów: ${zamowienie.sumaProduktow}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- PRZYCISK POBIERANIA PDF (Logika "Wszystko w jednym") ---
            OutlinedButton(
                onClick = {
                    isDownloading = true
                    scope.launch(Dispatchers.IO) {
                        try {
                            // Endpoint dla zaopatrzenia, który stworzyliśmy na backendzie
                            val pdfUrl = "${RetrofitInstance.BASE_URL}api/raporty/zaopatrzenie/pobierz/${zamowienie.id}"

                            val url = URL(pdfUrl)
                            val connection = url.openConnection() as HttpURLConnection
                            connection.connectTimeout = 8000
                            connection.readTimeout = 8000

                            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                                val bytes = connection.inputStream.readBytes()

                                // Zapis do publicznego folderu Downloads
                                val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                val file = File(downloadDir, "Zamowienie_Zao_${zamowienie.id}.pdf")
                                file.writeBytes(bytes)

                                // Odświeżenie skanera mediów, żeby plik był widoczny w systemie
                                android.media.MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null, null)

                                withContext(Dispatchers.Main) {
                                    isDownloading = false
                                    Toast.makeText(context, "Pobrano do folderu Pobrane", Toast.LENGTH_SHORT).show()

                                    // Otwieranie pliku
                                    try {
                                        val uri: Uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            setDataAndType(uri, "application/pdf")
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        context.startActivity(Intent.createChooser(intent, "Otwórz zamówienie"))
                                    } catch (e: Exception) {
                                        Log.e("PDF_OPEN", "Nie można otworzyć pliku: ${e.message}")
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    isDownloading = false
                                    Toast.makeText(context, "Błąd serwera: ${connection.responseCode}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                isDownloading = false
                                Log.e("PDF_ERROR", "Błąd pobierania: ${e.message}")
                                Toast.makeText(context, "Błąd połączenia z serwerem", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isDownloading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Pobieranie...")
                } else {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Pobierz Dokument PDF")
                }
            }

            // --- PRZYCISK ZMIANY STATUSU ---
            if (zamowienie.status != 2) {
                Button(
                    onClick = {
                        viewModel.zmienStatusZamowienia(zamowienie.id, 2, 4)
                        onNavigateBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Oznacz jako Zrealizowane")
                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Zamówienie zostało zrealizowane",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}