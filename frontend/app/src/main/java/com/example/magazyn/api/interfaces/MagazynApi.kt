package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.MagazynItemDTO
import retrofit2.http.GET

interface MagazynApi {
    @GET("api/magazyn/produkty")
    suspend fun getProdukty(): List<MagazynItemDTO>
}