package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.UzytkownikDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface UzytkownikApi {
    @GET("api/uzytkownicy/{id}")
    suspend fun getProfil(@Path("id") id: Int): UzytkownikDTO
}