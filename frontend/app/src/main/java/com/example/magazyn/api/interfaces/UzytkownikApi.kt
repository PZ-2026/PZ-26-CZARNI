package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.api.dtos.LoginRequest
import com.example.magazyn.api.dtos.LoginResponse
import com.example.magazyn.api.dtos.RegisterRequest
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UzytkownikApi {
    @GET("api/uzytkownicy/{id}")
    suspend fun getProfil(@Path("id") id: Int): UzytkownikDTO

    @POST("api/uzytkownicy/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/uzytkownicy/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("api/uzytkownicy/me")
    suspend fun getMe(@Header("Authorization") token: String): Response<UzytkownikDTO>
}