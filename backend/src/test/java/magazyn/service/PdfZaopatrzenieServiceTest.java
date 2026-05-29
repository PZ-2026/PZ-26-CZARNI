package magazyn.service;

import magazyn.entity.Dostawca;
import magazyn.entity.Produkt;
import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import magazyn.repository.ProduktRepository;
import magazyn.repository.ZamowieniaZaopatrzeniowiecRepository;
import magazyn.repository.ZamowienieProduktyDostawcyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfZaopatrzenieServiceTest {

    @Mock
    private ZamowieniaZaopatrzeniowiecRepository repository;

    @Mock
    private ZamowienieProduktyDostawcyRepository pozycjeRepository;

    @Mock
    private ProduktRepository produktRepository;

    // InjectMocks automatycznie wstrzykuje powyższe mocki do naszego serwisu
    @InjectMocks
    private PdfZaopatrzenieService pdfZaopatrzenieService;

    @Test
    void generatePurchaseOrderPdf_PowinnoZwrocicBajtowyPdf_KiedyZamowienieIstnieje() throws Exception {
        // Given (Przygotowanie danych)
        Integer idZamowienia = 1;

        ZamowienieZaopatrzeniowca zamowienie = new ZamowienieZaopatrzeniowca();
        zamowienie.setId(idZamowienia);
        zamowienie.setData(OffsetDateTime.now());
        zamowienie.setStatus(1);

        Dostawca dostawca = new Dostawca();
        dostawca.setNazwaDostawcy("Testowy Dostawca");
        dostawca.setAdres("Ul. Testowa 1, Warszawa");
        zamowienie.setDostawca(dostawca);

        ZamowienieProduktyDostawcy pozycja = new ZamowienieProduktyDostawcy();
        pozycja.setIdProduktu(100);
        pozycja.setIlosc(50);

        Produkt produkt = new Produkt();
        produkt.setNazwaProduktu("Młotek");

        // Mockowanie zachowania bazy danych
        when(repository.findById(idZamowienia)).thenReturn(Optional.of(zamowienie));
        when(pozycjeRepository.findByIdZamowienia(idZamowienia)).thenReturn(List.of(pozycja));
        when(produktRepository.findById(100)).thenReturn(Optional.of(produkt));

        // When (Wywołanie metody)
        byte[] result = pdfZaopatrzenieService.generatePurchaseOrderPdf(idZamowienia);

        // Then (Weryfikacja)
        assertNotNull(result, "Wygenerowany PDF nie powinien być nullem");
        assertTrue(result.length > 0, "Wygenerowany plik PDF nie powinien być pusty");

        // Sprawdzamy, czy serwis faktycznie odpytał repozytoria
        verify(repository, times(1)).findById(idZamowienia);
        verify(pozycjeRepository, times(1)).findByIdZamowienia(idZamowienia);
        verify(produktRepository, times(1)).findById(100);
    }

    @Test
    void generatePurchaseOrderPdf_PowinnoRzucicWyjatek_KiedyBrakZamowienia() {
        // Given
        Integer idZamowienia = 999;
        when(repository.findById(idZamowienia)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pdfZaopatrzenieService.generatePurchaseOrderPdf(idZamowienia);
        });

        assertTrue(exception.getMessage().contains("Nie znaleziono zamówienia"), "Komunikat błędu powinien być czytelny");

        // Upewniamy się, że w przypadku błędu nie szukamy produktów
        verify(pozycjeRepository, never()).findByIdZamowienia(anyInt());
    }
}