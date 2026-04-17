package magazyn.controllers;

import magazyn.entity.Produkt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import magazyn.repository.ProduktRepository;

import java.util.List;

@RestController
@RequestMapping("/api/magazyn")
public class MagazynController {

    @Autowired
    private ProduktRepository produktRepository;

    @GetMapping("/produkty")
    public List<Produkt> getAllProducts() {
        return produktRepository.findAll();
    }
}