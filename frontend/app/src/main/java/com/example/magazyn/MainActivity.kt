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
import com.example.magazyn.api.AuthTokenProvider
import com.example.magazyn.api.ApiConnector
import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.ui.theme.MagazynTheme
import com.example.magazyn.utils.AppSettings
import com.example.magazyn.utils.RoleConstants
import ui.screens.admin.AdminDashboard
import ui.screens.klient.KlientDashboard
import ui.screens.login.LoginScreen
import ui.screens.login.RegisterScreen
import ui.screens.magazynier.MagazynierDashboard
import ui.screens.zaopatrzeniowiec.ZaopatrzeniowiecDashboard
import androidx.compose.runtime.LaunchedEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            MagazynTheme(darkTheme = darkTheme) {
                var currentUser by remember { mutableStateOf<UzytkownikDTO?>(null) }
                var isCheckingSession by remember { mutableStateOf(true) }
                var isRegistering by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    val prefs = getSharedPreferences("sesja", MODE_PRIVATE)
                    val token = prefs.getString("auth_token", null)
                    darkTheme = AppSettings.isDarkMode(this@MainActivity)

                    if (token != null) {
                        AuthTokenProvider.token = token
                        val user = ApiConnector.weryfikujSesjeNaSerwerze(token)
                        if (user != null) {
                            currentUser = user // To wywoła recompozycję i pokaże Dashboard niżej
                        }
                    }
                    isCheckingSession = false
                }

                val handleLogout = {
                    // Czyścimy token przy wylogowaniu
                    getSharedPreferences("sesja", MODE_PRIVATE).edit().clear().apply()
                    AuthTokenProvider.token = null
                    currentUser = null
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isCheckingSession) {
                        PlaceholderScreen("Ładowanie sesji...", {})
                    } else if (isRegistering) {
                        RegisterScreen(
                            onRegisterSuccess = { isRegistering = false },
                            onBackToLogin = { isRegistering = false }
                        )
                    } else if (currentUser == null) {
                        LoginScreen(
                            onLoginSuccess = { user -> currentUser = user },
                            onNavigateToRegister = { isRegistering = true }
                        )
                    } else {
                        // Wyświetlanie dashboardów
                        when (currentUser?.rola) {
                            RoleConstants.ADMINISTRATOR -> AdminDashboard(handleLogout, darkTheme = darkTheme, onDarkModeChange = { enabled ->
                                darkTheme = enabled
                                AppSettings.setDarkMode(this@MainActivity, enabled)
                            })
                            RoleConstants.MAGAZYNIER -> MagazynierDashboard(currentUser!!, handleLogout)
                            RoleConstants.ZAOPATRZENIOWIEC -> ZaopatrzeniowiecDashboard(currentUser!!, handleLogout)
                            RoleConstants.KLIENT -> KlientDashboard(currentUser!!, handleLogout)
                            else -> PlaceholderScreen("Nieznana rola: ${currentUser?.rola}", handleLogout)
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
