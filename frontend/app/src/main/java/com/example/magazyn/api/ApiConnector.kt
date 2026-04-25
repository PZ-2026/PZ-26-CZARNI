package com.example.magazyn.api

import android.content.Context
import android.util.Log
import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.api.dtos.LoginRequest

object ApiConnector {
    suspend fun login(context: Context, login: String, haslo: String): UzytkownikDTO? {
        return try {
            val response = RetrofitInstance.uzytkownikApi.loginUser(LoginRequest(login, haslo))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // zapis tokenu
                    val prefs = context.getSharedPreferences("sesja", Context.MODE_PRIVATE)
                    prefs.edit().putString("auth_token", body.token).apply()

                    // zwracanie uzytkownika
                    return body.uzytkownik
                }
            }
            null
        } catch (e: Exception) {
            Log.e("ApiConnector", "Blad logowania: ${e.message}")
            null
        }

    }

    suspend fun weryfikujSesjeNaSerwerze(token: String): UzytkownikDTO? {
        return try {
            val response = RetrofitInstance.uzytkownikApi.getMe("Bearer $token")

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd weryfikacji sesji: ${e.message}")
            null
        }
    }
}