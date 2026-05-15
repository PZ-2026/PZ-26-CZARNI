package magazyn.controllers;

import magazyn.service.PdfGeneratorService;
import magazyn.repository.ZamowienieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/raporty")
public class PdfController {

    @Autowired
    private ZamowienieRepository zamowienieRepository;

    @GetMapping("/test-data/{id}")
    public ResponseEntity<?> testOrderData(@PathVariable Integer id) {
        return zamowienieRepository.findByIdWithProducts(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Autowired
    private PdfGeneratorService pdfService;

    @GetMapping("/pobierz/{id}")
    public ResponseEntity<byte[]> pobierzFakture(@PathVariable Integer id) {
        try {
            // Wywołujemy Twój poprawiony serwis
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
