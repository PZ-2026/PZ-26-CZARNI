package com.example.magazyn.api

import retrofit2.http.GET

interface MagazynApi {
    @GET("api/magazyn/produkty")
    suspend fun getProdukty(): List<MagazynItemDTO>
}