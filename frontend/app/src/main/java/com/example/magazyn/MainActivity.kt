package com.example.magazyn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.ui.theme.MagazynTheme
import ui.screens.admin.AdminDashboard
import ui.screens.klient.KlientDashboard
import ui.screens.login.LoginScreen
import ui.screens.zaopatrzeniowiec.ZaopatrzeniowiecDashboard
import ui.screens.login.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagazynTheme {
                var currentUser by remember { mutableStateOf<UzytkownikDTO?>(null) }
                var isRegistering by remember { mutableStateOf(false) }
                val handleLogout = { currentUser = null }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isRegistering) {
                        RegisterScreen(
                            onRegisterSuccess = { isRegistering = false },
                            onBackToLogin = { isRegistering = false }
                        )
                    } else {
                        if (currentUser == null) {
                            LoginScreen(
                                onLoginSuccess = { user -> currentUser = user },
                                onNavigateToRegister = { isRegistering = true }
                            )
                        }
                        else {
                            when (currentUser?.rola) {
                                0 -> KlientDashboard(handleLogout)
                                //1 -> MagazynierDashboard({ onLogout = handleLogout }) <-- jak kacperek zrobi to sie odkomentuje
                                2 -> ZaopatrzeniowiecDashboard(currentUser,handleLogout)
                                3 -> AdminDashboard(handleLogout)
                                else -> PlaceholderScreen("Nieznana rola", handleLogout)
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, fontSize = 24.sp)
        Button(onClick = onLogout) {
            Text("Wyloguj")
        }
    }
}
