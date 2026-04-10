package com.example.magazyn.ui.screens

import androidx.compose.foundation.layout.*
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
import com.example.magazyn.api.ApiConnector
import com.example.magazyn.DatabaseConnector

import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: (UserRole) -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        TextButton(
            onClick = {
                uriHandler.openUri("https://github.com/PZ-2026/PZ-26-CZARNI")
            },
            modifier = Modifier.align(Alignment.TopStart).padding(top = 40.dp, start = 16.dp)
        ) {
            Text(
                text = "Polityka prywatnosci",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                scope.launch {
                    // Wywołujemy nasze API zamiast bezpośredniego połączenia z DB
                    val rola = ApiConnector.login(login, password)

                    if (rola != null) {
                        // Jeśli API zwróciło rolę, mapujemy ją na enum
                        val userRole = when(rola) {
                            0 -> UserRole.KLIENT
                            1 -> UserRole.MAGAZYNIER
                            2 -> UserRole.ZAOPATRZENIOWIEC
                            3 -> UserRole.ADMIN
                            else -> UserRole.KLIENT
                        }
                        onLoginSuccess(userRole)
                    } else {

                        errorText = "Błędne dane logowania!"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Zaloguj się", fontSize = 18.sp, color = Color.White)
        }
    }
}