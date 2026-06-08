package magazyn.service;

import magazyn.entity.Uzytkownik;
import magazyn.entity.ZamowienieKlienta;
import magazyn.entity.ZamowienieProduktyKlienci;
import magazyn.entity.Produkt;
import magazyn.repository.ZamowienieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceTest {

    @Mock
    private ZamowienieRepository zamowienieRepository;

    @InjectMocks
    private PdfGeneratorService pdfGeneratorService;

    @Test
    void generateInvoice_PowinnoZwrocicBajtowyPdf_KiedyDaneSaPoprawne() throws Exception {
        // Given
        Integer idZamowienia = 5;

        Uzytkownik klient = new Uzytkownik();
        klient.setImie("Jan");
        klient.setNazwisko("Kowalski");
        klient.setFirma("Firma Testowa");
        klient.setNip("1234567890");
        klient.setTelefon("123-456-789");

        Produkt produkt = new Produkt();
        produkt.setNazwaProduktu("Gwoździe");
        produkt.setJednostka("kg");

        ZamowienieProduktyKlienci pozycja = new ZamowienieProduktyKlienci();
        pozycja.setProdukt(produkt);
        pozycja.setIlosc(10);
        pozycja.setCenaWDniuZakupu(new BigDecimal("15.50"));

        ZamowienieKlienta zamowienie = new ZamowienieKlienta();
        zamowienie.setId(idZamowienia);
        zamowienie.setData(OffsetDateTime.now());
        zamowienie.setKlient(klient);
        zamowienie.setPozycje(List.of(pozycja));

        when(zamowienieRepository.findByIdWithProducts(idZamowienia)).thenReturn(Optional.of(zamowienie));

        // When
        byte[] result = pdfGeneratorService.generateInvoice(idZamowienia);

        // Then
        assertNotNull(result);
        assertTrue(result.length > 0, "PDF z fakturą powinien zawierać bajty");
        verify(zamowienieRepository, times(1)).findByIdWithProducts(idZamowienia);
    }

    @Test
    void generateInvoice_PowinnoRzucicWyjatek_KiedyZamowienieNieIstnieje() {
        // Given
        Integer idZamowienia = 100;
        when(zamowienieRepository.findByIdWithProducts(idZamowienia)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pdfGeneratorService.generateInvoice(idZamowienia);
        });

        assertEquals("Nie znaleziono zamówienia o ID: 100", exception.getMessage());
    }
}