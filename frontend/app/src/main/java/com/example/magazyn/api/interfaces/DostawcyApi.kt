package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.DostawcaDTO
import retrofit2.http.GET

interface DostawcyApi {
    @GET("api/dostawcy")
    suspend fun getDostawcy(): List<DostawcaDTO>
}