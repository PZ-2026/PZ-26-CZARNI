package magazyn.controllers;

import magazyn.dto.NoweZamowienieRequest;
import magazyn.service.ZamowienieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zamowienia/zaopatrzenie")
public class ZamowieniaZaopatrzenieController {

    @Autowired
    private ZamowienieService zamowienieService;

    // Złożenie nowego zamówienia
    @PostMapping
    public ResponseEntity<String> dodajZamowienie(@RequestBody NoweZamowienieRequest request) {
        zamowienieService.zlozZamowienie(request);
        return ResponseEntity.ok("Zamówienie zostało złożone pomyślnie.");
    }
}