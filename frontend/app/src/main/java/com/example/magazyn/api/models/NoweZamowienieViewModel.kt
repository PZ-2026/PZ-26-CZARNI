package com.example.magazyn.api.models

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magazyn.api.DataSyncManager
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.NoweZamowienieRequest
import com.example.magazyn.api.dtos.PozycjaZamowienia
import com.example.magazyn.data.api.dtos.ProduktDTO
import kotlinx.coroutines.launch

class NoweZamowienieViewModel : ViewModel() {
    var produkty = mutableStateOf<List<ProduktDTO>>(emptyList())
    var isLoading = mutableStateOf(false)
    var isSubmitting = mutableStateOf(false)
    var successMessage = mutableStateOf<String?>(null)

    // Mapa przechowująca idProduktu -> wybrana ilość
    var koszyk = mutableStateMapOf<Int, Int>()

    // przechowuje aktualnie otwartego dostawcę
    var wybranyDostawcaId: Int? = null

    init {
        viewModelScope.launch {
            DataSyncManager.refreshTrigger.collect {
                // Jeśli dostawca jest wybrany, pobierz produkty ponownie
                wybranyDostawcaId?.let { pobierzProdukty(it) }
            }
        }
    }

    fun pobierzProdukty(idDostawcy: Int) {
        wybranyDostawcaId = idDostawcy

        viewModelScope.launch {
            isLoading.value = true
            try {
                produkty.value = RetrofitInstance.produktApi.getProduktyDostawcy(idDostawcy)

                // Dzięki temu, jeśli lista odświeży się w tle, nie wyzeruje to
                // ilości, którą użytkownik zdążył już wpisać.
                produkty.value.forEach {
                    if (!koszyk.containsKey(it.id)) {
                        koszyk[it.id] = 0
                    }
                }
            } catch (e: Exception) {
                Log.e("MagazynApp", "Błąd pobierania produktów: ", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun zmienIlosc(idProduktu: Int, nowaIlosc: Int) {
        if (nowaIlosc >= 0) {
            koszyk[idProduktu] = nowaIlosc
        }
    }

    fun zlozZamowienie(idDostawcy: Int, idUzytkownika: Int) {
        viewModelScope.launch {
            isSubmitting.value = true
            try {
                val pozycje = koszyk.filter { it.value > 0 }.map {
                    PozycjaZamowienia(it.key, it.value)
                }

                if (pozycje.isNotEmpty()) {
                    val request = NoweZamowienieRequest(idDostawcy, idUzytkownika, pozycje)
                    RetrofitInstance.zamowieniaApi.zlozZamowienie(request)
                    successMessage.value = "Zamówienie wysłane!"
                }
            } catch (e: Exception) {
                Log.e("MagazynApp", "Błąd wysyłania zamówienia: ", e)
            } finally {
                isSubmitting.value = false
            }
        }
    }

    fun wyczyscStan() {
        successMessage.value = null
        koszyk.clear()
        wybranyDostawcaId = null
    }
}