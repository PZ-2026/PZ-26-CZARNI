package magazyn.controllers;

import magazyn.service.PdfGeneratorService;
import magazyn.repository.ZamowienieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler odpowiedzialny za generowanie i pobieranie raportów w formacie PDF.
 */
@RestController
@RequestMapping("/api/raporty")
public class PdfController {

    @Autowired
    private ZamowienieRepository zamowienieRepository;

    /**
     * Endpoint testowy do sprawdzenia danych zamówienia przed generowaniem PDF.
     *
     * @param id identyfikator zamówienia
     * @return dane zamówienia lub status 404
     */
    @GetMapping("/test-data/{id}")
    public ResponseEntity<?> testOrderData(@PathVariable Integer id) {
        return zamowienieRepository.findByIdWithProducts(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    private PdfGeneratorService pdfService;

    /**
     * Generuje i zwraca fakturę w formacie PDF dla konkretnego zamówienia.
     *
     * @param id identyfikator zamówienia
     * @return dokument PDF jako tablica bajtów w odpowiedzi HTTP
     */
    @GetMapping("/pobierz/{id}")
    public ResponseEntity<byte[]> pobierzFakture(@PathVariable Integer id) {
        try {
            // Wywołujemy serwis generujący PDF
            byte[] pdfContents = pdfService.generateInvoice(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // "inline" otworzy w przeglądarce, "attachment" od razu pobierze na dysk
            headers.setContentDisposition(ContentDisposition.builder("inline")
                    .filename("faktura_" + id + ".pdf")
                    .build());

            // Cache-Control zapobiega problemom z zapamiętywaniem starej wersji PDF przez przeglądarkę
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
