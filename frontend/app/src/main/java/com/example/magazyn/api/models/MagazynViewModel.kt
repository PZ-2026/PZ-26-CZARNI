package com.example.magazyn.api.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.MagazynItemDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MagazynViewModel : ViewModel() {
    // Lista produktów, którą będzie obserwował widok
    private val _produkty = MutableStateFlow<List<MagazynItemDTO>>(emptyList())
    val produkty: StateFlow<List<MagazynItemDTO>> = _produkty

    init {
        pobierzProdukty()
    }

    fun pobierzProdukty() {
        viewModelScope.launch {
            try {
                // Wywołujemy nasze API przez RetrofitInstance
                val odpowiedz = RetrofitInstance.magazynApi.getProdukty()
                _produkty.value = odpowiedz
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
