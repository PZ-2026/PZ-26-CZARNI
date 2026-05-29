package magazyn.controllers;

import jakarta.transaction.Transactional;
import magazyn.dto.HistoriaZamowieniaDTO;
import magazyn.dto.NoweZamowienieRequest;
import magazyn.dto.PozycjaZamowieniaResponse;
import magazyn.entity.StanMagazynu;
import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import magazyn.repository.HistoriaKlientaRepository;
import magazyn.repository.StanMagazynuRepository;
import magazyn.repository.ZamowieniaZaopatrzeniowiecRepository;
import magazyn.repository.ZamowienieProduktyDostawcyRepository;
import magazyn.service.ZamowienieKlientService;
import magazyn.service.ZamowienieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Kontroler obsługujący zamówienia klientów oraz zaopatrzeniowców.
 * Zarządza historią zamówień, składaniem nowych zamówień oraz zmianą ich statusu.
 */
@RestController
@RequestMapping("/api/zamowienia")
public class ZamowieniaController {

    @Autowired
    private ZamowieniaZaopatrzeniowiecRepository repository;

    @Autowired
    private ZamowienieProduktyDostawcyRepository pozycjeRepository;

    @Autowired
    private StanMagazynuRepository stanMagazynuRepository;

    @Autowired
    private HistoriaKlientaRepository historiaKlientaRepository;

    @Autowired
    private ZamowienieKlientService zamowienieKlientService;

    @Autowired
    private ZamowienieService zamowienieService;

    /**
     * Pobiera historię zamówień dla konkretnego użytkownika (zaopatrzeniowca).
     *
     * @param uzytkownikId identyfikator użytkownika
     * @return lista obiektów DTO reprezentujących historię zamówień
     */
    @GetMapping("/historia/{uzytkownikId}")
    public List<HistoriaZamowieniaDTO> getHistoria(@PathVariable("uzytkownikId") Integer uzytkownikId) {
        return repository.findHistoriaByUzytkownik(uzytkownikId);
    }

    /**
     * Pobiera historię zamówień dla konkretnego klienta.
     *
     * @param uzytkownikId identyfikator klienta
     * @return lista obiektów DTO reprezentujących historię zamówień klienta
     */
    @GetMapping("historiaklient/{uzytkownikId}")
    public List<HistoriaZamowieniaDTO> getHistoriaKlient(@PathVariable Integer uzytkownikId) {
        return historiaKlientaRepository.findHistoriaKlienta(uzytkownikId);
    }

    /**
     * Zmienia status zamówienia zaopatrzeniowca. Jeśli status zmienia się na skompletowany (2),
     * stany magazynowe produktów z tego zamówienia zostają zwiększone.
     *
     * @param id identyfikator zamówienia
     * @param nowyStatus nowy status do ustawienia
     * @return odpowiedź HTTP OK lub NOT FOUND
     */
    @PutMapping("/{id}/status")
    @Transactional
    public ResponseEntity<?> zmienStatusZamowienia(
            @PathVariable Integer id,
            @RequestParam Integer nowyStatus
    ) {
        Optional<ZamowienieZaopatrzeniowca> zamowienieOpt = repository.findById(id);

        if (zamowienieOpt.isPresent()) {
            ZamowienieZaopatrzeniowca zamowienie = zamowienieOpt.get();

            if (nowyStatus == 2 && zamowienie.getStatus() != 2) {

                List<ZamowienieProduktyDostawcy> pozycje = pozycjeRepository.findByIdZamowienia(id);

                for (ZamowienieProduktyDostawcy pozycja : pozycje) {
                    Optional<StanMagazynu> stanOpt = stanMagazynuRepository.findByProdukt_Id(pozycja.getIdProduktu());

                    if (stanOpt.isPresent()) {
                        StanMagazynu stan = stanOpt.get();

                        java.math.BigDecimal iloscZZamowienia = new java.math.BigDecimal(pozycja.getIlosc());

                        // Dodajemy ilości do siebie używając metody .add()
                        java.math.BigDecimal nowaIlosc = stan.getIlosc().add(iloscZZamowienia);

                        // Ustawiamy i zapisujemy
                        stan.setIlosc(nowaIlosc);
                        stanMagazynuRepository.save(stan);
                    }
                }
            }

            zamowienie.setStatus(nowyStatus);
            repository.save(zamowienie);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Składa nowe zamówienie przez klienta.
     *
     * @param request obiekt zawierający dane nowego zamówienia
     * @return odpowiedź HTTP z informacją o powodzeniu lub błędzie (np. brak towaru)
     */
    @PostMapping("/klient")
    public ResponseEntity<String> zlozZamowienie(@RequestBody NoweZamowienieRequest request) {
        try {
            zamowienieKlientService.zlozZamowienie(request);
            return ResponseEntity.ok("Zamówienie złożone!"); // OK 200
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Conflict 409
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błąd serwera"); // Error 500
        }
    }

    /**
     * Pobiera listę pozycji dla konkretnego zamówienia.
     *
     * @param id identyfikator zamówienia
     * @return odpowiedź HTTP z listą pozycji zamówienia
     */
    @GetMapping("/{id}/pozycje")
    public ResponseEntity<List<PozycjaZamowieniaResponse>> getPozycje(@PathVariable Integer id) {
        return ResponseEntity.ok(zamowienieService.getPozycjeDlaZamowienia(id));
    }
}