package magazyn.controllers;

import magazyn.service.PdfZaopatrzenieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler odpowiedzialny za generowanie raportów PDF dla zamówień zaopatrzeniowych.
 */
@RestController
@RequestMapping("/api/raporty/zaopatrzenie")
public class PdfZaopatrzenieController {

    @Autowired
    private PdfZaopatrzenieService pdfService;

    /**
     * Generuje i udostępnia do pobrania plik PDF z zamówieniem zaopatrzeniowym.
     *
     * @param id identyfikator zamówienia zaopatrzeniowego
     * @return dokument PDF jako tablica bajtów z nagłówkiem wymuszającym pobranie pliku
     */
    @GetMapping("/pobierz/{id}")
    public ResponseEntity<byte[]> pobierzPdfZamowienia(@PathVariable Integer id) {
        try {
            byte[] pdfContents = pdfService.generatePurchaseOrderPdf(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // "attachment" sprawi, że plik od razu się pobierze
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("zamowienie_zaop_" + id + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfContents, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
