package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.HistoriaZamowieniaDTO
import com.example.magazyn.api.dtos.NoweZamowienieRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ZamowieniaApi {

    @GET("api/zamowienia/historia/{uzytkownikId}")
    suspend fun getHistoriaZamowien(
        @Path("uzytkownikId") id: Int
    ): List<HistoriaZamowieniaDTO>

    @POST("api/zamowienia/zaopatrzenie")
    suspend fun zlozZamowienie(@Body request: NoweZamowienieRequest)

    @PUT("api/zamowienia/{id}/status")
    suspend fun zmienStatusZamowienia(
        @Path("id") id: Int,
        @Query("nowyStatus") nowyStatus: Int
    ): Response<Unit>

    @GET("api/zamowienia/historiaklient/{uzytkownikId}")
    suspend fun getHistoriaZamowienKlient(
        @Path("uzytkownikId") id: Int
    ): List<HistoriaZamowieniaDTO>
}