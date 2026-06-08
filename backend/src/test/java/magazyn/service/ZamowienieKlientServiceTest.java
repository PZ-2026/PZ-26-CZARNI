package magazyn.service;

import magazyn.dto.NoweZamowienieRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZamowienieKlientServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ZamowienieKlientService zamowienieKlientService;

    // =========================================================
    // TEST 1: Złożenie zamówienia kończy się sukcesem
    // =========================================================
    @Test
    void zlozZamowienie_PowinnoPrzejscPomyslnie_KiedySaWszystkieDaneITowar() {
        // Given
        Integer idKlienta = 5;
        Integer idMagazyniera = 12;
        Integer idNowegoZamowienia = 100;

        NoweZamowienieRequest.PozycjaZamowienia pozycja = new NoweZamowienieRequest.PozycjaZamowienia();
        pozycja.setIdProduktu(1);
        pozycja.setIlosc(10);

        NoweZamowienieRequest request = new NoweZamowienieRequest();
        request.setIdUzytkownika(idKlienta);
        request.setPozycje(List.of(pozycja));

        // 1. Mockowanie szukania magazyniera (metoda z 2 argumentami)
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
                .thenReturn(idMagazyniera);

        // 2. Mockowanie wstawiania głównego zamówienia (metoda z 4 argumentami: sql, Class, param1, param2)
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any()))
                .thenReturn(idNowegoZamowienia);

        // 3. Mockowanie pobierania ceny produktu (metoda z 3 argumentami: sql, Class, param1)
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), any()))
                .thenReturn(25.50);

        // Mockowanie aktualizacji stanu magazynowego
        when(jdbcTemplate.update(anyString(), eq(pozycja.getIlosc()), eq(pozycja.getIdProduktu()), eq(pozycja.getIlosc())))
                .thenReturn(1);

        // Mockowanie insertu łączącego
        when(jdbcTemplate.update(anyString(), eq(idNowegoZamowienia), eq(pozycja.getIdProduktu()), eq(pozycja.getIlosc()), eq(25.50)))
                .thenReturn(1);

        // When & Then
        assertDoesNotThrow(() -> zamowienieKlientService.zlozZamowienie(request));

        // Weryfikacja struktur
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class));
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(), any());
    }

    // =========================================================
    // TEST 2: Rzucanie błędu, gdy brakuje towaru w magazynie
    // =========================================================
    @Test
    void zlozZamowienie_PowinnoRzucicWyjatek_KiedyBrakujeTowaruNaStanie() {
        // Given
        NoweZamowienieRequest.PozycjaZamowienia pozycja = new NoweZamowienieRequest.PozycjaZamowienia();
        pozycja.setIdProduktu(99);
        pozycja.setIlosc(500);

        NoweZamowienieRequest request = new NoweZamowienieRequest();
        request.setIdUzytkownika(1);
        request.setPozycje(List.of(pozycja));

        // 1. Szukanie magazyniera
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
                .thenReturn(2);

        // 2. Wstawianie głównego zamówienia
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any()))
                .thenReturn(101);

        // 3. Pobieranie ceny produktu
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), any()))
                .thenReturn(10.0);

        // Stan magazynowy zwraca 0 wierszy (czyli brak wystarczającej ilości towaru)
        when(jdbcTemplate.update(anyString(), eq(500), eq(99), eq(500)))
                .thenReturn(0);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            zamowienieKlientService.zlozZamowienie(request);
        });

        assertEquals("Brak wystarczającej ilości produktu o ID: 99", exception.getMessage());

        // Sprawdzenie, czy zapobiegliśmy zapisaniu pozycji
        verify(jdbcTemplate, never()).update(anyString(), eq(101), eq(99), eq(500), eq(10.0));
    }

    // =========================================================
    // TEST 3: Przypisanie NULL, gdy nie ma żadnego magazyniera
    // =========================================================
    @Test
    void zlozZamowienie_PowinnoUstawicMagazynieraNull_KiedyZadnegoNieZnaleziono() {
        // Given
        NoweZamowienieRequest request = new NoweZamowienieRequest();
        request.setIdUzytkownika(1);
        request.setPozycje(List.of()); // Pusta lista dla uproszczenia testu

        // 1. Szukanie magazyniera rzuca wyjątek o braku danych
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        // 2. Wstawianie głównego zamówienia (powinno przyjąć argumenty i zwrócić ID nowego zamówienia)
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), any(), any()))
                .thenReturn(200);

        // When & Then
        assertDoesNotThrow(() -> zamowienieKlientService.zlozZamowienie(request));

        // Weryfikujemy, czy wywołano insert dla zamówienia
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Integer.class), any(), any());
    }
}