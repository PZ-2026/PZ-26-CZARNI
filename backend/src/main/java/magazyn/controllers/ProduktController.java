package magazyn.controllers;

import magazyn.entity.Produkt;
import magazyn.repository.ProduktRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Kontroler obsługujący żądania związane z produktami.
 */
@RestController
@RequestMapping("/api/produkty")
public class ProduktController {

    @Autowired
    private ProduktRepository produktRepo;

    /**
     * Pobiera listę produktów należących do określonego dostawcy.
     *
     * @param idDostawcy identyfikator dostawcy
     * @return lista produktów przypisanych do dostawcy
     */
    @GetMapping("/dostawca/{idDostawcy}")
    public List<Produkt> getProduktyDostawcy(@PathVariable Integer idDostawcy) {
        return produktRepo.findByIdDostawcy(idDostawcy);
    }
}