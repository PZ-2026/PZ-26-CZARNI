package com.example.magazyn.api.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.DostawcaDTO
import kotlinx.coroutines.launch

class DostawcyViewModel : ViewModel() {
    var dostawcyList = mutableStateOf<List<DostawcaDTO>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun fetchDostawcy() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            try {
                // Wywołanie API
                val response = RetrofitInstance.dostawcyApi.getDostawcy()
                dostawcyList.value = response
            } catch (e: Exception) {
                errorMessage.value = "Błąd pobierania danych: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}