package ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.RegisterRequest
import com.example.magazyn.ui.theme.DeepBurgundy
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    // Stany dla wszystkich pól
    var imie by remember { mutableStateOf("") }
    var nazwisko by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var haslo by remember { mutableStateOf("") }
    var nazwaFirmy by remember { mutableStateOf("") }
    var nip by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val commonModifier = Modifier.fillMaxWidth().widthIn(max = 400.dp)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(scrollState), // Dodane przewijanie
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Przy wielu polach lepiej zacząć od góry
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Rejestracja",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Grupa pól tekstowych
            OutlinedTextField(
                value = imie,
                onValueChange = { imie = it },
                label = { Text("Imię") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nazwisko,
                onValueChange = { nazwisko = it },
                label = { Text("Nazwisko") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = telefon,
                onValueChange = { telefon = it },
                label = { Text("Telefon") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = haslo,
                onValueChange = { haslo = it },
                label = { Text("Hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nazwaFirmy,
                onValueChange = { nazwaFirmy = it },
                label = { Text("Nazwa firmy") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nip,
                onValueChange = { nip = it },
                label = { Text("NIP") },
                modifier = commonModifier,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            // Główny przycisk rejestracji
            Button(
                onClick = {
                    val request = RegisterRequest(
                        imie = imie,
                        nazwisko = nazwisko,
                        telefon = telefon,
                        email = email,
                        haslo = haslo,
                        firma = nazwaFirmy.ifBlank { null },
                        nip = nip.ifBlank { null }
                    )

                    scope.launch {
                        isLoading = true
                        errorMessage = null

                        try {
                            val response = RetrofitInstance.uzytkownikApi.register(request)
                            if (response.isSuccessful) {
                                snackbarHostState.showSnackbar(
                                    message = "Pomyślnie zarejestrowano",
                                    duration = SnackbarDuration.Short
                                )
                                kotlinx.coroutines.delay(1500)
                                onRegisterSuccess()
                            } else {
                                val errorText = response.errorBody()?.string() ?: "Wystąpił błąd"
                                errorMessage = errorText
                            }
                        } catch (e: Exception) {
                            errorMessage = "Błąd połączenia: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = commonModifier.height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Zarejestruj się", fontSize = 18.sp, color = Color.White)
                }
            }

            // Powrót do logowania
            TextButton(
                onClick = { onBackToLogin() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Masz już konto? Zaloguj się",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
