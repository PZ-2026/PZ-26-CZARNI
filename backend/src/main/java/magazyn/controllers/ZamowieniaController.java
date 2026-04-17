package magazyn.controllers;

import jakarta.transaction.Transactional;
import magazyn.dto.HistoriaZamowieniaDTO;
import magazyn.entity.StanMagazynu;
import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import magazyn.repository.StanMagazynuRepository;
import magazyn.repository.ZamowieniaZaopatrzeniowiecRepository;
import magazyn.repository.ZamowienieProduktyDostawcyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/zamowienia")
public class ZamowieniaController {

    @Autowired
    private ZamowieniaZaopatrzeniowiecRepository repository;

    @Autowired
    private ZamowienieProduktyDostawcyRepository pozycjeRepository;

    @Autowired
    private StanMagazynuRepository stanMagazynuRepository;

    @GetMapping("/historia/{uzytkownikId}")
    public List<HistoriaZamowieniaDTO> getHistoria(@PathVariable Integer uzytkownikId) {
        return repository.findHistoriaByUzytkownik(uzytkownikId);
    }

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
}