package com.example.magazyn.api.interfaces

import com.example.magazyn.api.dtos.*
import retrofit2.Response
import retrofit2.http.*

interface AdminApi {
    // ============ UŻYTKOWNICY ============
    @GET("api/admin/users")
    suspend fun getAllUsers(): Response<List<UzytkownikAdminDTO>>

    @GET("api/admin/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UzytkownikAdminDTO>

    @POST("api/admin/users")
    suspend fun createUser(@Body user: UzytkownikAdminDTO): Response<UzytkownikAdminDTO>

    @PUT("api/admin/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UzytkownikAdminDTO): Response<UzytkownikAdminDTO>

    @DELETE("api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>

    @PUT("api/admin/users/{id}/block")
    suspend fun blockUser(@Path("id") id: Int): Response<Unit>

    @PUT("api/admin/users/{id}/unblock")
    suspend fun unblockUser(@Path("id") id: Int): Response<Unit>

    @GET("api/admin/users/role/{roleId}")
    suspend fun getUsersByRole(@Path("roleId") roleId: Int): Response<List<UzytkownikAdminDTO>>

    // ============ ROLE ============
    @PUT("api/admin/users/{id}/role/{newRoleId}")
    suspend fun updateUserRole(
        @Path("id") userId: Int,
        @Path("newRoleId") newRoleId: Int
    ): Response<UzytkownikAdminDTO>

    @GET("api/admin/roles/statistics")
    suspend fun getRoleStatistics(): Response<Map<String, Int>>

    // ============ FINANSE ============
    @GET("api/admin/financial/report")
    suspend fun getFinancialReport(
        @Query("dataPoczatek") dataPoczatek: String?,
        @Query("dataKoniec") dataKoniec: String?
    ): Response<RaportFinansowyDTO>

    @GET("api/admin/financial/revenue-month")
    suspend fun getMonthlyRevenue(
        @Query("rok") rok: Int?,
        @Query("miesiac") miesiac: Int?
    ): Response<Map<String, Any>>

    @GET("api/admin/financial/expenses-month")
    suspend fun getMonthlyExpenses(
        @Query("rok") rok: Int?,
        @Query("miesiac") miesiac: Int?
    ): Response<Map<String, Any>>

    @GET("api/admin/financial/profit-month")
    suspend fun getMonthlyProfit(
        @Query("rok") rok: Int?,
        @Query("miesiac") miesiac: Int?
    ): Response<Map<String, Any>>

    @GET("api/admin/financial/history")
    suspend fun getFinancialHistory(
        @Query("limit") limit: Int = 100
    ): Response<List<DaneFinansoweDTO>>

    @POST("api/admin/financial/entry")
    suspend fun addFinancialEntry(@Body entry: DaneFinansoweDTO): Response<DaneFinansoweDTO>

    // ============ KONFIGURACJA ============
    @GET("api/admin/configuration")
    suspend fun getAllConfiguration(): Response<List<KonfiguracijaDTO>>

    @GET("api/admin/configuration/active")
    suspend fun getActiveConfiguration(): Response<List<KonfiguracijaDTO>>

    @GET("api/admin/configuration/{parametr}")
    suspend fun getConfiguration(@Path("parametr") parametr: String): Response<KonfiguracijaDTO>

    @POST("api/admin/configuration")
    suspend fun createConfiguration(@Body config: KonfiguracijaDTO): Response<KonfiguracijaDTO>

    @PUT("api/admin/configuration/{id}")
    suspend fun updateConfiguration(
        @Path("id") id: Int,
        @Body config: KonfiguracijaDTO
    ): Response<KonfiguracijaDTO>

    @DELETE("api/admin/configuration/{id}")
    suspend fun deleteConfiguration(@Path("id") id: Int): Response<Unit>

    // ============ PANEL ADMINA (DASHBOARD) ============
    @GET("api/admin/dashboard")
    suspend fun getDashboard(): Response<PanelAdminaDTO>
}
