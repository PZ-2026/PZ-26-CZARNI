package com.example.magazyn.api

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiConnector {
    private const val API_URL = "http://188.68.236.147:3000/login"

    suspend fun login(email: String, haslo: String): Int? = withContext(Dispatchers.IO) {
        try {
            val url = URL(API_URL)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.connectTimeout = 5000

            // Tworzymy paczkę JSON do wysłania
            val jsonInput = JSONObject().apply {
                put("email", email)
                put("haslo", haslo)
            }

            // Wysyłamy dane
            conn.outputStream.use { it.write(jsonInput.toString().toByteArray()) }

            // Sprawdzamy odpowiedź
            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                return@withContext jsonResponse.getInt("rola")
            }
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}