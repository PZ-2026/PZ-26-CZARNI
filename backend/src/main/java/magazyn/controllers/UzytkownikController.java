package magazyn.controllers;

import magazyn.entity.Uzytkownik;
import magazyn.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody java.util.Map<String, String> credentials) {
        String login = credentials.get("login");
        String haslo = credentials.get("haslo");

        return uzytkownikRepository.findByEmail(login)
                .map(user -> {
                    if (user.getHaslo().equals(haslo)) {
                        return ResponseEntity.ok(user);
                    }
                    else {
                        return ResponseEntity.status(401).body("Błędne haslo");
                    }
                        })
                .orElse(ResponseEntity.status(404).body("Błędne dane logowania."));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Uzytkownik nowyUzytkownik) {
        try {
            if (uzytkownikRepository.findByEmail(nowyUzytkownik.getEmail()).isPresent()) {
                return ResponseEntity.status(400).body("Podany email jest zajety");
            }

            Uzytkownik zapisanyUzytkownik = uzytkownikRepository.save(nowyUzytkownik);
            return ResponseEntity.ok(zapisanyUzytkownik);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas rejestracji: " + e.getMessage());
        }
    }
}