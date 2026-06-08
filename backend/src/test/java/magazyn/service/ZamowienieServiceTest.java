package magazyn.service;

import magazyn.dto.NoweZamowienieRequest;
import magazyn.dto.PozycjaZamowieniaResponse;
import magazyn.entity.*;
import magazyn.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZamowienieServiceTest {

    @Mock
    private ZamowieniaZaopatrzeniowiecRepository zamowieniaRepo;
    @Mock
    private ZamowienieProduktyDostawcyRepository pozycjeRepo;
    @Mock
    private DostawcaRepository dostawcaRepo;
    @Mock
    private UzytkownikRepository uzytkownikRepo;
    @Mock
    private ProduktRepository produktRepository;

    @InjectMocks
    private ZamowienieService zamowienieService;

    // =========================================================
    // TESTY: zlozZamowienie
    // =========================================================

    @Test
    void zlozZamowienie_PowinnoZapisacZamowienieIPozycje_KiedyIloscPozycjiJestWiekszaNizZero() {
        // Given
        NoweZamowienieRequest request = new NoweZamowienieRequest();
        request.setIdDostawcy(1);
        request.setIdUzytkownika(2);

        NoweZamowienieRequest.PozycjaZamowienia poz = new NoweZamowienieRequest.PozycjaZamowienia();
        poz.setIdProduktu(10);
        poz.setIlosc(5); // Ilość > 0
        request.setPozycje(List.of(poz));

        ZamowienieZaopatrzeniowca zapisaneZamowienie = new ZamowienieZaopatrzeniowca();
        zapisaneZamowienie.setId(500);

        when(dostawcaRepo.getReferenceById(1)).thenReturn(new Dostawca());
        when(uzytkownikRepo.getReferenceById(2)).thenReturn(new Uzytkownik());
        when(zamowieniaRepo.save(any(ZamowienieZaopatrzeniowca.class))).thenReturn(zapisaneZamowienie);

        // When
        assertDoesNotThrow(() -> zamowienieService.zlozZamowienie(request));

        // Then
        verify(zamowieniaRepo, times(1)).save(any(ZamowienieZaopatrzeniowca.class));
        verify(pozycjeRepo, times(1)).save(any(ZamowienieProduktyDostawcy.class));
    }

    @Test
    void zlozZamowienie_PowinnoPominacZapisPozycji_KiedyIloscWynosiZero() {
        // Given
        NoweZamowienieRequest request = new NoweZamowienieRequest();
        request.setIdDostawcy(1);
        request.setIdUzytkownika(2);

        NoweZamowienieRequest.PozycjaZamowienia poz = new NoweZamowienieRequest.PozycjaZamowienia();
        poz.setIdProduktu(10);
        poz.setIlosc(0); // Ilość równa 0!
        request.setPozycje(List.of(poz));

        ZamowienieZaopatrzeniowca zapisaneZamowienie = new ZamowienieZaopatrzeniowca();
        zapisaneZamowienie.setId(500);

        when(zamowieniaRepo.save(any(ZamowienieZaopatrzeniowca.class))).thenReturn(zapisaneZamowienie);

        // When
        assertDoesNotThrow(() -> zamowienieService.zlozZamowienie(request));

        // Then
        verify(zamowieniaRepo, times(1)).save(any(ZamowienieZaopatrzeniowca.class));
        // Pozycja repo nie powinna zostać wywołana, bo ilość nie była > 0
        verify(pozycjeRepo, never()).save(any(ZamowienieProduktyDostawcy.class));
    }

    // =========================================================
    // TESTY: getPozycjeDlaZamowienia
    // =========================================================

    @Test
    void getPozycjeDlaZamowienia_PowinnoZwrocicPelneDane_KiedyProduktyIstnieja() {
        // Given
        Integer idZamowienia = 100;

        ZamowienieProduktyDostawcy pozBaza = new ZamowienieProduktyDostawcy();
        pozBaza.setIdProduktu(15);
        pozBaza.setIlosc(30);

        Produkt produktBaza = new Produkt();
        produktBaza.setId(15);
        produktBaza.setNazwaProduktu("Karton zbiorczy");

        when(pozycjeRepo.findByIdZamowienia(idZamowienia)).thenReturn(List.of(pozBaza));
        when(produktRepository.findAllById(List.of(15))).thenReturn(List.of(produktBaza));

        // When
        List<PozycjaZamowieniaResponse> wynik = zamowienieService.getPozycjeDlaZamowienia(idZamowienia);

        // Then
        assertNotNull(wynik);
        assertEquals(1, wynik.size());
        assertEquals("Karton zbiorczy", wynik.get(0).getNazwaProduktu());
        assertEquals(30, wynik.get(0).getIlosc());
    }

    @Test
    void getPozycjeDlaZamowienia_PowinnoZwrocicPlaceholder_KiedyProduktuNieMaWBazie() {
        // Given
        Integer idZamowienia = 100;

        ZamowienieProduktyDostawcy pozBaza = new ZamowienieProduktyDostawcy();
        pozBaza.setIdProduktu(999); // ID nieistniejącego produktu
        pozBaza.setIlosc(10);

        when(pozycjeRepo.findByIdZamowienia(idZamowienia)).thenReturn(List.of(pozBaza));
        // Symulujemy, że baza nie znalazła żadnego produktu dla tego ID (zwraca pustą listę)
        when(produktRepository.findAllById(List.of(999))).thenReturn(Collections.emptyList());

        // When
        List<PozycjaZamowieniaResponse> wynik = zamowienieService.getPozycjeDlaZamowienia(idZamowienia);

        // Then
        assertNotNull(wynik);
        assertEquals(1, wynik.size());
        assertEquals("Nieznany produkt (ID: 999)", wynik.get(0).getNazwaProduktu());
        assertEquals(10, wynik.get(0).getIlosc());
    }
}