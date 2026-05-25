package com.example.magazyn.api

import android.content.Context
import android.util.Log
import com.example.magazyn.api.dtos.*
import retrofit2.http.*

object ApiConnector {
    suspend fun login(context: Context, login: String, haslo: String): UzytkownikDTO? {
        return try {
            val response = RetrofitInstance.uzytkownikApi.loginUser(LoginRequest(login, haslo))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val prefs = context.getSharedPreferences("sesja", Context.MODE_PRIVATE)
                    prefs.edit().putString("auth_token", body.token).apply()
                    AuthTokenProvider.token = body.token
                    return body.uzytkownik
                }
            }
            null
        } catch (e: Exception) { null }
    }

    suspend fun weryfikujSesjeNaSerwerze(token: String): UzytkownikDTO? {
        return try {
            val response = RetrofitInstance.uzytkownikApi.getMe("Bearer $token")
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzWszystkowUzytkownikow(): List<UzytkownikAdminDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllUsers()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun utworzUzytkownika(user: UzytkownikAdminDTO): Pair<UzytkownikAdminDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.createUser(user)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }

    // Nowa wersja edycji przyjmująca mapę pól
    suspend fun edytujUzytkownika(id: Int, pola: Map<String, Any?>): Pair<UzytkownikAdminDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.updateUser(id, pola)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }

    suspend fun usunUzytkownika(id: Int): Boolean {
        return try { RetrofitInstance.adminApi.deleteUser(id).isSuccessful } catch (e: Exception) { false }
    }

    suspend fun zablokowakUzytkownika(id: Int): Boolean {
        return try { RetrofitInstance.adminApi.blockUser(id).isSuccessful } catch (e: Exception) { false }
    }

    suspend fun odblokowakUzytkownika(id: Int): Boolean {
        return try { RetrofitInstance.adminApi.unblockUser(id).isSuccessful } catch (e: Exception) { false }
    }

    suspend fun pobierzRaportFinansowy(dataPoczatek: String?, dataKoniec: String?): RaportFinansowyDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getFinancialReport(dataPoczatek, dataKoniec)
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzPrzychodyMiesiac(): Double? {
        return try {
            val response = RetrofitInstance.adminApi.getRevenueMonth()
            if (response.isSuccessful) response.body()?.get("przychody") else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzWydatkiMiesiac(): Double? {
        return try {
            val response = RetrofitInstance.adminApi.getExpensesMonth()
            if (response.isSuccessful) response.body()?.get("wydatki") else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzZyskMiesiac(): Double? {
        return try {
            val response = RetrofitInstance.adminApi.getProfitMonth()
            if (response.isSuccessful) response.body()?.get("zysk") else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzDanePanelu(): PanelAdminaDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getDashboard()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun pobierzWszystkoKonfiguracje(): List<KonfiguracijaDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllConfiguration()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    // ============ DOSTAWCY ============

    suspend fun pobierzWszystkichDostawcow(): List<DostawcaDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllDostawcy()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun utworzDostawce(dostawca: DostawcaDTO): Pair<DostawcaDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.createDostawca(dostawca)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }

    suspend fun edytujDostawce(id: Int, dostawca: DostawcaDTO): Pair<DostawcaDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.updateDostawca(id, dostawca)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }

    suspend fun usunDostawce(id: Int): Boolean {
        return try { RetrofitInstance.adminApi.deleteDostawca(id).isSuccessful } catch (e: Exception) { false }
    }

    // ============ STAN MAGAZYNU ============

    suspend fun pobierzCalyStanMagazynu(): List<StanMagazynuDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllStanMagazynu()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) { null }
    }

    suspend fun edytujStanMagazynu(id: Int, stan: StanMagazynuDTO): Pair<StanMagazynuDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.updateStanMagazynu(id, stan)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }

    // ============ ZAMÓWIENIA (dla panelu/admina) ============

    suspend fun pobierzHistorieZamowien(uzytkownikId: Int): List<HistoriaZamowieniaDTO>? {
        return try {
            RetrofitInstance.zamowieniaApi.getHistoriaZamowienKlient(uzytkownikId)
        } catch (e: Exception) { null }
    }

    suspend fun zmienStatusZamowienia(id: Int, nowyStatus: Int): Boolean {
        return try {
            val response = RetrofitInstance.zamowieniaApi.zmienStatusZamowienia(id, nowyStatus)
            response.isSuccessful
        } catch (e: Exception) { false }
    }

    // ============ KONFIGURACJA ============

    suspend fun edytujKonfiguracje(id: Int, config: KonfiguracijaDTO): Pair<KonfiguracijaDTO?, String?> {
        return try {
            val response = RetrofitInstance.adminApi.updateKonfiguracja(id, config)
            if (response.isSuccessful) Pair(response.body(), null)
            else Pair(null, response.errorBody()?.string() ?: "Błąd serwera")
        } catch (e: Exception) { Pair(null, e.message) }
    }
}
