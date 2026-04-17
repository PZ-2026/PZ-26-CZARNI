package com.example.magazyn.api

import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.api.dtos.LoginRequest

object ApiConnector {
    suspend fun login(login: String, haslo: String): UzytkownikDTO? {
        val response = RetrofitInstance.uzytkownikApi.loginUser(LoginRequest(login, haslo))
        return if (response.isSuccessful) response.body() else null
    }
}