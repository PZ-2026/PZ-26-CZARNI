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
                    // zapis tokenu
                    val prefs = context.getSharedPreferences("sesja", Context.MODE_PRIVATE)
                    prefs.edit().putString("auth_token", body.token).apply()

                    // zwracanie uzytkownika
                    return body.uzytkownik
                }
            }
            null
        } catch (e: Exception) {
            Log.e("ApiConnector", "Blad logowania: ${e.message}")
            null
        }

    }

    suspend fun weryfikujSesjeNaSerwerze(token: String): UzytkownikDTO? {
        return try {
            val response = RetrofitInstance.uzytkownikApi.getMe("Bearer $token")

            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd weryfikacji sesji: ${e.message}")
            null
        }
    }

    // ============ ADMIN - UŻYTKOWNICY ============
    suspend fun pobierzWszystkowUzytkownikow(): List<UzytkownikAdminDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllUsers()
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ApiConnector", "Błąd pobierania użytkowników: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania użytkowników: ${e.message}")
            null
        }
    }

    suspend fun pobierzUzytkownikaPoId(id: Int): UzytkownikAdminDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getUserById(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania użytkownika: ${e.message}")
            null
        }
    }

    suspend fun utworzUzytkownika(user: UzytkownikAdminDTO): UzytkownikAdminDTO? {
        return try {
            val response = RetrofitInstance.adminApi.createUser(user)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("ApiConnector", "Błąd tworzenia użytkownika: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd tworzenia użytkownika: ${e.message}")
            null
        }
    }

    suspend fun edytujUzytkownika(id: Int, user: UzytkownikAdminDTO): UzytkownikAdminDTO? {
        return try {
            val response = RetrofitInstance.adminApi.updateUser(id, user)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd edytowania użytkownika: ${e.message}")
            null
        }
    }

    suspend fun usunUzytkownika(id: Int): Boolean {
        return try {
            val response = RetrofitInstance.adminApi.deleteUser(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd usuwania użytkownika: ${e.message}")
            false
        }
    }

    suspend fun zablokowakUzytkownika(id: Int): Boolean {
        return try {
            val response = RetrofitInstance.adminApi.blockUser(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd blokowania użytkownika: ${e.message}")
            false
        }
    }

    suspend fun odblokowakUzytkownika(id: Int): Boolean {
        return try {
            val response = RetrofitInstance.adminApi.unblockUser(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd odblokowania użytkownika: ${e.message}")
            false
        }
    }

    suspend fun pobierzUzytkownikaPodleRoli(roleId: Int): List<UzytkownikAdminDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getUsersByRole(roleId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania użytkowników: ${e.message}")
            null
        }
    }

    // ============ ADMIN - ROLE ============
    suspend fun zmienRoleUzytkownika(userId: Int, newRoleId: Int): UzytkownikAdminDTO? {
        return try {
            val response = RetrofitInstance.adminApi.updateUserRole(userId, newRoleId)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd zmiany roli: ${e.message}")
            null
        }
    }

    suspend fun pobierzStatystykeRol(): Map<String, Int>? {
        return try {
            val response = RetrofitInstance.adminApi.getRoleStatistics()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania statystyki ról: ${e.message}")
            null
        }
    }

    // ============ ADMIN - FINANSE ============
    suspend fun pobierzRaportFinansowy(dataPoczatek: String?, dataKoniec: String?): RaportFinansowyDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getFinancialReport(dataPoczatek, dataKoniec)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania raportu finansowego: ${e.message}")
            null
        }
    }

    suspend fun pobierzPrzychodyMiesiac(rok: Int?, miesiac: Int?): Map<String, Any>? {
        return try {
            val response = RetrofitInstance.adminApi.getMonthlyRevenue(rok, miesiac)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania przychodów: ${e.message}")
            null
        }
    }

    suspend fun pobierzWydatkiMiesiac(rok: Int?, miesiac: Int?): Map<String, Any>? {
        return try {
            val response = RetrofitInstance.adminApi.getMonthlyExpenses(rok, miesiac)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania wydatków: ${e.message}")
            null
        }
    }

    suspend fun pobierzZyskMiesiac(rok: Int?, miesiac: Int?): Map<String, Any>? {
        return try {
            val response = RetrofitInstance.adminApi.getMonthlyProfit(rok, miesiac)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania zysku: ${e.message}")
            null
        }
    }

    suspend fun pobierzHistorieFinansowa(limit: Int = 100): List<DaneFinansoweDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getFinancialHistory(limit)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania historii finansowej: ${e.message}")
            null
        }
    }

    suspend fun dodajWpisFinansowy(entry: DaneFinansoweDTO): DaneFinansoweDTO? {
        return try {
            val response = RetrofitInstance.adminApi.addFinancialEntry(entry)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd dodawania wpisu finansowego: ${e.message}")
            null
        }
    }

    // ============ ADMIN - KONFIGURACJA ============
    suspend fun pobierzWszystkoKonfiguracje(): List<KonfiguracijaDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getAllConfiguration()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania konfiguracji: ${e.message}")
            null
        }
    }

    suspend fun pobierzAktywneKonfiguracje(): List<KonfiguracijaDTO>? {
        return try {
            val response = RetrofitInstance.adminApi.getActiveConfiguration()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania aktywnej konfiguracji: ${e.message}")
            null
        }
    }

    suspend fun pobierzKonfiguracje(parametr: String): KonfiguracijaDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getConfiguration(parametr)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania konfiguracji: ${e.message}")
            null
        }
    }

    suspend fun utworzKonfiguracje(config: KonfiguracijaDTO): KonfiguracijaDTO? {
        return try {
            val response = RetrofitInstance.adminApi.createConfiguration(config)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd tworzenia konfiguracji: ${e.message}")
            null
        }
    }

    suspend fun edytujKonfiguracje(id: Int, config: KonfiguracijaDTO): KonfiguracijaDTO? {
        return try {
            val response = RetrofitInstance.adminApi.updateConfiguration(id, config)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd edytowania konfiguracji: ${e.message}")
            null
        }
    }

    suspend fun usunKonfiguracje(id: Int): Boolean {
        return try {
            val response = RetrofitInstance.adminApi.deleteConfiguration(id)
            response.isSuccessful
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd usuwania konfiguracji: ${e.message}")
            false
        }
    }

    // ============ ADMIN - PANEL ============
    suspend fun pobierzDanePanelu(): PanelAdminaDTO? {
        return try {
            val response = RetrofitInstance.adminApi.getDashboard()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ApiConnector", "Błąd pobierania danych panelu: ${e.message}")
            null
        }
    }
}