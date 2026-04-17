package magazyn.controllers;

import magazyn.entity.Uzytkownik;
import magazyn.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Uzytkownik> getProfil(@PathVariable Integer id) {
        return uzytkownikRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}