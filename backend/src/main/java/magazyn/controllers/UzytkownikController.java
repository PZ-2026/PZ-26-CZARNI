package magazyn.controllers;

import jakarta.validation.Valid;
import magazyn.entity.Sesja;
import magazyn.entity.Uzytkownik;
import magazyn.repository.SesjaRepository;
import magazyn.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private SesjaRepository sesjaRepository;


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
                        String token = java.util.UUID.randomUUID().toString();

                        Sesja nowaSesja = new Sesja(token, user, 30);
                        sesjaRepository.save(nowaSesja);

                        java.util.Map<String, Object> response = new java.util.HashMap<>();
                        response.put("uzytkownik", user);
                        response.put("token", token);

                        return ResponseEntity.ok(response);
                    }
                    else {
                        return ResponseEntity.status(401).body("Błędne haslo");
                    }
                        })
                .orElse(ResponseEntity.status(404).body("Błędne dane logowania."));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Uzytkownik nowyUzytkownik, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
            }

            if (uzytkownikRepository.findByEmail(nowyUzytkownik.getEmail()).isPresent()) {
                return ResponseEntity.status(400).body("Podany email jest zajety");
            }

            Uzytkownik zapisanyUzytkownik = uzytkownikRepository.save(nowyUzytkownik);
            return ResponseEntity.ok(zapisanyUzytkownik);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas rejestracji: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String token) {
        String cleanToken = token.replace("Bearer ", "");

        return sesjaRepository.findByToken(cleanToken)
                .map(sesja -> {
                    if (sesja.getDataWygasniecia().isAfter(LocalDateTime.now())) {
                        return ResponseEntity.ok(sesja.getUzytkownik());
                    }
                    return ResponseEntity.status(401).body("Sesja wygasła");
                })
                .orElse(ResponseEntity.status(401).body("Nieprawidłowy token"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Uzytkownik> updateUzytkownik(@PathVariable Integer id, @RequestBody Uzytkownik dane) {
        return uzytkownikRepository.findById(id)
                .map(uzytkownik -> {
                    // Przepisujemy dane z DTO/Body do obiektu encji
                    uzytkownik.setImie(dane.getImie());
                    uzytkownik.setNazwisko(dane.getNazwisko());
                    uzytkownik.setEmail(dane.getEmail());
                    uzytkownik.setTelefon(dane.getTelefon());
                    uzytkownik.setFirma(dane.getFirma());
                    uzytkownik.setNip(dane.getNip());

                    // Zapisujemy w Postgresie
                    Uzytkownik zapisany = uzytkownikRepository.save(uzytkownik);
                    return ResponseEntity.ok(zapisany);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}