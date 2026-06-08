package magazyn.service;

import magazyn.dto.KonfiguracijaDTO;
import magazyn.dto.PanelAdminaDTO;
import magazyn.dto.UzytkownikAdminDTO;
import magazyn.entity.Konfiguracja;
import magazyn.entity.Uzytkownik;
import magazyn.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UzytkownikRepository uzytkownikRepository;

    @Mock
    private DaneFinansoweRepository daneFinansoweRepository;

    @Mock
    private KonfiguracijaRepository konfiguracijaRepository;

    @Mock
    private ProduktRepository produktRepository;

    @InjectMocks
    private AdminService adminService;

    // ==========================================
    // TESTY UŻYTKOWNIKÓW
    // ==========================================

    @Test
    void utworzUzytkownika_PowinnoStworzycUzytkownika_KiedyEmailJestUnikalny() {
        // Given
        UzytkownikAdminDTO requestDTO = new UzytkownikAdminDTO();
        requestDTO.setEmail("nowy@test.pl");
        requestDTO.setImie("Jan");
        requestDTO.setRola(2);

        Uzytkownik zapisanyUzytkownik = new Uzytkownik();
        zapisanyUzytkownik.setId(10);
        zapisanyUzytkownik.setEmail("nowy@test.pl");
        zapisanyUzytkownik.setImie("Jan");
        zapisanyUzytkownik.setRola(2);
        zapisanyUzytkownik.setZablokowany(false);

        when(uzytkownikRepository.findByEmail("nowy@test.pl")).thenReturn(Optional.empty());
        when(uzytkownikRepository.save(any(Uzytkownik.class))).thenReturn(zapisanyUzytkownik);

        // When
        UzytkownikAdminDTO result = adminService.utworzUzytkownika(requestDTO);

        // Then
        assertNotNull(result);
        assertEquals(10, result.getId());
        assertEquals("nowy@test.pl", result.getEmail());
        verify(uzytkownikRepository, times(1)).save(any(Uzytkownik.class));
    }

    @Test
    void utworzUzytkownika_PowinnoRzucicWyjatek_KiedyEmailJestZajety() {
        // Given
        UzytkownikAdminDTO requestDTO = new UzytkownikAdminDTO();
        requestDTO.setEmail("istniejacy@test.pl");

        when(uzytkownikRepository.findByEmail("istniejacy@test.pl")).thenReturn(Optional.of(new Uzytkownik()));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.utworzUzytkownika(requestDTO);
        });

        assertEquals("Użytkownik z tym emailem już istnieje", exception.getMessage());
        verify(uzytkownikRepository, never()).save(any(Uzytkownik.class));
    }

    @Test
    void zablokowakUzytkownika_PowinnoZmienicStatusNaZablokowany() {
        // Given
        Integer userId = 5;
        Uzytkownik uzytkownik = new Uzytkownik();
        uzytkownik.setId(userId);
        uzytkownik.setZablokowany(false);

        when(uzytkownikRepository.findById(userId)).thenReturn(Optional.of(uzytkownik));

        // When
        adminService.zablokowakUzytkownika(userId);

        // Then
        assertTrue(uzytkownik.getZablokowany(), "Użytkownik powinien zostać zablokowany");
        verify(uzytkownikRepository, times(1)).save(uzytkownik);
    }

    // ==========================================
    // TESTY KONFIGURACJI
    // ==========================================

    @Test
    void utworzKonfiguracje_PowinnoDodacParametr_KiedyNazwaJestUnikalna() {
        // Given
        KonfiguracijaDTO dto = new KonfiguracijaDTO();
        dto.setNazwaParametru("MAX_ZAMOWIEN");
        dto.setWartoscParametru("100");

        Konfiguracja konfiguracjaZapisana = new Konfiguracja();
        konfiguracjaZapisana.setId(1);
        konfiguracjaZapisana.setNazwaParametru("MAX_ZAMOWIEN");
        konfiguracjaZapisana.setWartoscParametru("100");
        konfiguracjaZapisana.setAktywna(true);

        when(konfiguracijaRepository.findByNazwaParametru("MAX_ZAMOWIEN")).thenReturn(Optional.empty());
        when(konfiguracijaRepository.save(any(Konfiguracja.class))).thenReturn(konfiguracjaZapisana);

        // When
        KonfiguracijaDTO result = adminService.utworzKonfiguracje(dto);

        // Then
        assertEquals("MAX_ZAMOWIEN", result.getNazwaParametru());
        assertTrue(result.getAktywna(), "Nowa konfiguracja domyślnie powinna być aktywna");
    }

    // ==========================================
    // TESTY FINANSÓW I PANELU ADMINA (Złożone)
    // ==========================================

    @Test
    void pobierzPrzychodyMiesiac_PowinnoZwrocicZero_KiedyBrakDanych() {
        // Given
        // Mockujemy zapytanie do bazy, aby zwróciło null (np. gdy nie było transakcji w danym miesiącu)
        when(daneFinansoweRepository.sumaPrzychodow(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(null);

        // When
        BigDecimal result = adminService.pobierzPrzychodyMiesiac();

        // Then
        assertEquals(BigDecimal.ZERO, result, "Jeśli baza zwraca null, serwis powinien zwrócić BigDecimal.ZERO");
    }

    @Test
    void pobierzDanePanelu_PowinnoZebracWszystkieStatystyki() {
        // Given
        when(uzytkownikRepository.count()).thenReturn(150L);
        when(produktRepository.count()).thenReturn(50L);

        // Używamy any(), ponieważ serwis generuje zakres dat z 'YearMonth.now()' wewnątrz metody
        when(daneFinansoweRepository.sumaPrzychodow(any(), any())).thenReturn(new BigDecimal("10000.00"));
        when(daneFinansoweRepository.sumaWydatkow(any(), any())).thenReturn(new BigDecimal("2000.00"));
        when(daneFinansoweRepository.sumaZysku(any(), any())).thenReturn(new BigDecimal("8000.00"));

        // When
        PanelAdminaDTO panel = adminService.pobierzDanePanelu();

        // Then
        assertEquals(150, panel.getLiczbaUzytkownikow());
        assertEquals(50, panel.getLiczbaProduktu());
        assertEquals(new BigDecimal("10000.00"), panel.getPrzychodyMiesiac());
        assertEquals(new BigDecimal("8000.00"), panel.getZyskMiesiac());

        // Sprawdzamy czy metody wywołały odpowiednie repozytoria
        verify(uzytkownikRepository, times(1)).count();
        verify(produktRepository, times(1)).count();
    }
}