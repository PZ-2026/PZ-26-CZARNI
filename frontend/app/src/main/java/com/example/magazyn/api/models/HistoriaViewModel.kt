package com.example.magazyn.api.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magazyn.api.DataSyncManager
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.HistoriaZamowieniaDTO
import kotlinx.coroutines.launch

class HistoriaViewModel : ViewModel() {
    // Stan przechowujący listę zamówień
    var historiaList = mutableStateOf<List<HistoriaZamowieniaDTO>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchHistoria(uzytkownikId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                // Wywołanie API
                val response = RetrofitInstance.zamowieniaApi.getHistoriaZamowien(uzytkownikId)
                historiaList.value = response
            } catch (e: Exception) {
                errorMessage.value = "Błąd pobierania danych: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun zmienStatusZamowienia(zamowienieId: Int, nowyStatus: Int, uzytkownikId: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response =
                    RetrofitInstance.zamowieniaApi.zmienStatusZamowienia(zamowienieId, nowyStatus)
                if (response.isSuccessful) {
                    fetchHistoria(uzytkownikId)
                    DataSyncManager.triggerUpdate()
                } else {
                    errorMessage.value = "Błąd zmiany statusu: kod ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Błąd połączenia: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }

    }
}