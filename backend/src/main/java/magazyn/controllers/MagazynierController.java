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

@RestController
@RequestMapping("/api/magazynier")
public class MagazynierController {

    @Autowired
    private ZamowienieKlientaRepository zamowienieKlientaRepository;

    @Autowired
    private ZamowienieProduktyKlienciRepository zamowienieProduktyKlienciRepository;

    @GetMapping("/zamowienia/{magazynierId}")
    public List<ZamowienieKlientaDTO> getZamowieniaDoSpakowania(@PathVariable Integer magazynierId) {
        List<ZamowienieKlienta> zamowienia = zamowienieKlientaRepository.findByMagazynierIdAndStatusNot(magazynierId, 2);
        return zamowienia.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @PutMapping("/zamowienia/{id}/status")
    public ResponseEntity<?> zmienStatus(@PathVariable Integer id, @RequestParam Integer status) {
        return zamowienieKlientaRepository.findById(id).map(z -> {
            z.setStatus(status);
            zamowienieKlientaRepository.save(z);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private ZamowienieKlientaDTO convertToDTO(ZamowienieKlienta z) {
        List<ZamowienieProduktyKlienci> pozycje = zamowienieProduktyKlienciRepository.findByZamowienieId(z.getId());
        List<ZamowienieKlientaDTO.PozycjaZamowieniaDTO> produkty = pozycje.stream()
                .map(p -> new ZamowienieKlientaDTO.PozycjaZamowieniaDTO(
                        p.getProdukt().getId(),
                        p.getProdukt().getNazwaProduktu(),
                        p.getIlosc(),
                        p.getProdukt().getKodKreskowy()
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
