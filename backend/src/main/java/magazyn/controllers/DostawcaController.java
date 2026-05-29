package magazyn.controllers;

import magazyn.entity.Dostawca;
import magazyn.repository.DostawcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontroler odpowiedzialny za zarządzanie danymi dostawców.
 */
@RestController
@RequestMapping("/api/dostawcy")
public class DostawcaController {

    @Autowired
    private DostawcaRepository dostawcaRepository;

    /**
     * Pobiera listę wszystkich dostawców zarejestrowanych w systemie.
     *
     * @return Lista wszystkich dostawców.
     */
    @GetMapping
    public List<Dostawca> getWszyscyDostawcy() {
        return dostawcaRepository.findAll();
    }
}