package com.example.magazyn.api.models

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.HistoriaZamowieniaDTO
import com.example.magazyn.api.dtos.MagazynItemDTO
import com.example.magazyn.api.dtos.NoweZamowienieRequest
import com.example.magazyn.api.dtos.PozycjaZamowienia
import com.example.magazyn.api.dtos.UzytkownikDTO
import com.example.magazyn.data.api.dtos.ProduktDTO
import kotlinx.coroutines.launch

class KlientViewModel : ViewModel() {
    var id: Int? = null
    var imie by mutableStateOf("")
    var nazwisko by mutableStateOf("")
    var email by mutableStateOf("")
    var telefon by mutableStateOf("")
    var firma by mutableStateOf("")
    var nip by mutableStateOf("")
    var rola: Int? = null

    var isEditing by mutableStateOf(false)
    var isLoading by mutableStateOf(true)

    var produkty by mutableStateOf<List<MagazynItemDTO>>(emptyList())

    var wybraneIlosci = mutableStateMapOf<Int, Int>()

    val sumaKoszyka by derivedStateOf {
        wybraneIlosci.entries.sumOf { (id, ilosc) ->
            val produkt = produkty.find {it.id == id }
            val cena = produkt?.cena ?: 0.0
            cena * ilosc
        }
    }

    var searchQuery by mutableStateOf("")

    val filtrowaneProdukty by derivedStateOf {
        if (searchQuery.isEmpty()) {
            produkty
        } else {
            produkty.filter { it.nazwaProduktu.contains(searchQuery, ignoreCase = true) }
        }
    }

    var isSending by mutableStateOf(false)

    fun setup(user: UzytkownikDTO?) {
        if (user == null) return
        id = user.id
        imie = user.imie
        nazwisko = user.nazwisko
        email = user.email
        telefon = user.telefon
        firma = user.firma!!
        nip = user.nip!!
        rola = user.rola
        isLoading = false
    }

    fun zapisz() {
        val aktualneId = id
        val aktualnaRola = rola
        if (aktualneId == null || aktualnaRola == null) return

        isLoading = true
        val uzytkownikDTO = UzytkownikDTO(
            aktualneId,
            imie,
            nazwisko,
            email,
            telefon,
            firma,
            nip,
            aktualnaRola
        )
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.uzytkownikApi.update(uzytkownikDTO.id, uzytkownikDTO)
                if (response.isSuccessful) {
                    isEditing = false
                } else {
                    Log.e("API_ERROR", "Błąd: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK_ERROR", "Błąd sieci: ${e.message}")
            }
        }
    }

    fun onButtonClicked() {
        if (isEditing) {
            zapisz()
        }
        else {
            isEditing = true
        }
    }

    fun pobierzProdukty(){
        viewModelScope.launch {
            try {
                produkty = RetrofitInstance.magazynApi.getProdukty()
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }

    fun aktualizujIlosc(idProduktu: Int, nowaIlosc: Int, stanMagazynowy: Int) {
        if (nowaIlosc <= 0) {
            wybraneIlosci.remove(idProduktu)
        } else if (nowaIlosc <= stanMagazynowy) {
            wybraneIlosci[idProduktu] = nowaIlosc
        } else {
            // Opcjonalnie: tutaj możesz dodać jakiś komunikat,
            // że nie ma tyle towaru, ale na razie po prostu blokujemy zmianę.
        }
    }

    fun zlozZamowienie(idKlienta: Int, onStatus: (String) -> Unit) {
        if (wybraneIlosci.isEmpty()) return

        isSending = true
        val request = NoweZamowienieRequest(
            idDostawcy = 0, // tak --------
            idUzytkownika = idKlienta,
            pozycje = wybraneIlosci.map {(id, ilosc) ->
                PozycjaZamowienia(id, ilosc)
            }
        )

        viewModelScope.launch {
            try{
                val response = RetrofitInstance.zamowieniaApi.zlozZamowienieKlient(request)
                if (response.isSuccessful) {
                    wybraneIlosci.clear()
                    pobierzProdukty()
                    onStatus("Zamowienie złożone!")
                } else {
                    onStatus("Błąd serwera: ${response.code()}")
                }
            } catch (e: Exception) {
                onStatus("Błąd sieci: ${e.message}")
            }
            isSending = false
        }
    }
}