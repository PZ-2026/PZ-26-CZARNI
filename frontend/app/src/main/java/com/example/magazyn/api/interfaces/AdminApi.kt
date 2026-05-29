package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApi {
    @GET("api/admin/users")
    suspend fun getAllUsers(): Response<List<UzytkownikAdminDTO>>

    @GET("api/admin/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UzytkownikAdminDTO>

    @POST("api/admin/users")
    suspend fun createUser(@Body user: UzytkownikAdminDTO): Response<UzytkownikAdminDTO>

    @PUT("api/admin/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body user: Map<String, @JvmSuppressWildcards Any?>
    ): Response<UzytkownikAdminDTO>

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>

    @POST("api/admin/users/{id}/block")
    suspend fun blockUser(@Path("id") id: Int): Response<Unit>

    @POST("api/admin/users/{id}/unblock")
    suspend fun unblockUser(@Path("id") id: Int): Response<Unit>

    @GET("api/admin/dashboard")
    suspend fun getDashboard(): Response<PanelAdminaDTO>

    @GET("api/admin/financial/report")
    suspend fun getFinancialReport(@Query("dataPoczatek") dataPoczatek: String?, @Query("dataKoniec") dataKoniec: String?): Response<RaportFinansowyDTO>

    @GET("api/admin/financial/revenue-month")
    suspend fun getRevenueMonth(): Response<Map<String, Double>>

    @GET("api/admin/financial/expenses-month")
    suspend fun getExpensesMonth(): Response<Map<String, Double>>

    @GET("api/admin/financial/profit-month")
    suspend fun getProfitMonth(): Response<Map<String, Double>>

    @GET("api/admin/configuration")
    suspend fun getAllConfiguration(): Response<List<KonfiguracijaDTO>>

    @PUT("api/admin/configuration/{id}")
    suspend fun updateKonfiguracja(@Path("id") id: Int, @Body config: KonfiguracijaDTO): Response<KonfiguracijaDTO>

    // ============ DOSTAWCY ============

    @GET("api/admin/dostawcy")
    suspend fun getAllDostawcy(): Response<List<DostawcaDTO>>

    @GET("api/admin/dostawcy/{id}")
    suspend fun getDostawcaById(@Path("id") id: Int): Response<DostawcaDTO>

    @POST("api/admin/dostawcy")
    suspend fun createDostawca(@Body dostawca: DostawcaDTO): Response<DostawcaDTO>

    @PUT("api/admin/dostawcy/{id}")
    suspend fun updateDostawca(@Path("id") id: Int, @Body dostawca: DostawcaDTO): Response<DostawcaDTO>

    @DELETE("api/admin/dostawcy/{id}")
    suspend fun deleteDostawca(@Path("id") id: Int): Response<Unit>

    // ============ STAN MAGAZYNU ============

    @GET("api/admin/magazyn")
    suspend fun getAllStanMagazynu(): Response<List<StanMagazynuDTO>>

    @POST("api/admin/magazyn")
    suspend fun createStanMagazynu(@Body stan: StanMagazynuDTO): Response<StanMagazynuDTO>

    @GET("api/admin/magazyn/produkt/{idProduktu}")
    suspend fun getStanProduktu(@Path("idProduktu") idProduktu: Int): Response<StanMagazynuDTO>

    @PUT("api/admin/magazyn/{id}")
    suspend fun updateStanMagazynu(@Path("id") id: Int, @Body stan: StanMagazynuDTO): Response<StanMagazynuDTO>

    @DELETE("api/admin/magazyn/{id}")
    suspend fun deleteStanMagazynu(@Path("id") id: Int): Response<Unit>

    @GET("api/admin/financial/history")
    suspend fun getFinancialHistory(@Query("dataPoczatek") dataPoczatek: String, @Query("dataKoniec") dataKoniec: String): Response<List<DaneFinansoweDTO>>
}
