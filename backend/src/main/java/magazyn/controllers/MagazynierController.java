package magazyn.controllers;

import magazyn.dto.ZamowienieKlientaDTO;
import magazyn.entity.ZamowienieKlienta;
import magazyn.entity.ZamowienieProduktyKlienci;
import magazyn.repository.ZamowienieKlientaRepository;
import magazyn.repository.ZamowienieProduktyKlienciRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Kontroler obsługujący operacje wykonywane przez magazyniera.
 */
@RestController
@RequestMapping("/api/magazynier")
public class MagazynierController {

    @Autowired
    private ZamowienieKlientaRepository zamowienieKlientaRepository;

    @Autowired
    private ZamowienieProduktyKlienciRepository zamowienieProduktyKlienciRepository;

    /**
     * Pobiera listę zamówień przypisanych do konkretnego magazyniera, które nie mają jeszcze statusu skompletowanego.
     *
     * @param magazynierId identyfikator magazyniera
     * @return lista zamówień do spakowania w formacie DTO
     */
    @GetMapping("/zamowienia/{magazynierId}")
    public List<ZamowienieKlientaDTO> getZamowieniaDoSpakowania(@PathVariable Integer magazynierId) {
        List<ZamowienieKlienta> zamowienia = zamowienieKlientaRepository.findByMagazynierIdAndStatusNot(magazynierId, 2);
        return zamowienia.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Zmienia status konkretnego zamówienia.
     *
     * @param id identyfikator zamówienia
     * @param status nowy status zamówienia (np. 1 - w pakowaniu, 2 - skompletowane)
     * @return odpowiedź HTTP OK w przypadku sukcesu lub NOT FOUND w przypadku braku zamówienia
     */
    @PutMapping("/zamowienia/{id}/status")
    public ResponseEntity<?> zmienStatus(@PathVariable Integer id, @RequestParam Integer status) {
        return zamowienieKlientaRepository.findById(id).map(z -> {
            z.setStatus(status);
            zamowienieKlientaRepository.save(z);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Pomocnicza metoda do konwersji encji zamówienia na obiekt DTO przesyłany do frontendu.
     * Pobiera pozycje zamówienia i przypisuje im strefy magazynowe.
     *
     * @param z encja zamówienia klienta
     * @return obiekt DTO zamówienia
     */
    private ZamowienieKlientaDTO convertToDTO(ZamowienieKlienta z) {
        List<ZamowienieProduktyKlienci> pozycje = zamowienieProduktyKlienciRepository.findByZamowienieId(z.getId());
        List<ZamowienieKlientaDTO.PozycjaZamowieniaDTO> produkty = pozycje.stream()
                .map(p -> new ZamowienieKlientaDTO.PozycjaZamowieniaDTO(
                        p.getProdukt().getId(),
                        p.getProdukt().getNazwaProduktu(),
                        p.getIlosc(),
                        p.getProdukt().getKodKreskowy(),
                        p.getProdukt().getStrefa()
                )).collect(Collectors.toList());

        return new ZamowienieKlientaDTO(
                z.getId(),
                z.getData(),
                z.getKlient().getImie(),
                z.getKlient().getNazwisko(),
                z.getStatus(),
                produkty
        );
    }
}
