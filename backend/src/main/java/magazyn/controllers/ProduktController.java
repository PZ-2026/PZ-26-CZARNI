package magazyn.controllers;

import magazyn.entity.Produkt;
import magazyn.repository.ProduktRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produkty")
public class ProduktController {

    @Autowired
    private ProduktRepository produktRepo;

    @GetMapping("/dostawca/{idDostawcy}")
    public List<Produkt> getProduktyDostawcy(@PathVariable Integer idDostawcy) {
        return produktRepo.findByIdDostawcy(idDostawcy);
    }
}