package ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magazyn.UserRole
import com.example.magazyn.ui.theme.DeepBurgundy
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun LoginScreen(onLoginSuccess: (UserRole) -> Unit, onNavigateToRegister: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // KONTENER OGRANICZAJĄCY SZEROKOŚĆ (MaxWidth 400dp)
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp) // Kluczowa linia dla tabletów
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(150.dp))

            Text(
                text = "Magazyn App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Hasło") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            if (errorText.isNotEmpty()) {
                Text(text = errorText, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // loginy na sztywno

                    when {

                        login == "admin" && password == "123" -> onLoginSuccess(UserRole.ADMIN)

                        login == "zao" && password == "123" -> onLoginSuccess(UserRole.ZAOPATRZENIOWIEC)

                        login == "mag" && password == "123" -> onLoginSuccess(UserRole.MAGAZYNIER)

                        login == "klient" && password == "123" -> onLoginSuccess(UserRole.KLIENT)

                        else -> errorText = "Błędne dane logowania!"

                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Zaloguj się", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Przyciski pod spodem - teraz w Column, żeby na tabletach były jeden pod drugim
            // lub w Row, jeśli wolisz obok siebie:
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { uriHandler.openUri("https://github.com/PZ-2026/PZ-26-CZARNI") }) {
                    Text("Polityka prywatności", color = Color.Gray, fontSize = 14.sp)
                }
                TextButton(onClick = { onNavigateToRegister() }) { // TUTAJ ZMIANA
                    Text("Zarejestruj się", color = Color.Gray, fontSize = 14.sp)
                }
            }
        }
    }
}