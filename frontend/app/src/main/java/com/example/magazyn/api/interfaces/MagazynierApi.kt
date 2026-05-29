package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.ZamowienieKlientaDTO
import retrofit2.Response
import retrofit2.http.*

interface MagazynierApi {
    @GET("api/magazynier/zamowienia/{magazynierId}")
    suspend fun getZamowieniaDoSpakowania(
        @Path("magazynierId") magazynierId: Int
    ): List<ZamowienieKlientaDTO>

    @PUT("api/magazynier/zamowienia/{id}/status")
    suspend fun zmienStatusZamowienia(
        @Path("id") id: Int,
        @Query("status") status: Int
    ): Response<Unit>
}
