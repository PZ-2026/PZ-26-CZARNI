package com.example.magazyn.api.interfaces

import com.example.magazyn.data.api.dtos.ProduktDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ProduktApi {

    // Ten endpoint pobiera tylko produkty przypisane do klikniętego dostawcy
    @GET("api/produkty/dostawca/{idDostawcy}")
    suspend fun getProduktyDostawcy(
        @Path("idDostawcy") idDostawcy: Int
    ): List<ProduktDTO>

    //endpoint do pobierania wszystkich produktów
    @GET("api/produkty")
    suspend fun getWszystkieProdukty(): List<ProduktDTO>
}