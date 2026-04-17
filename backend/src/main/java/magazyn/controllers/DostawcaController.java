package magazyn.controllers;

import magazyn.entity.Dostawca;
import magazyn.repository.DostawcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dostawcy")
public class DostawcaController {

    @Autowired
    private DostawcaRepository dostawcaRepository;

    @GetMapping
    public List<Dostawca> getWszyscyDostawcy() {
        return dostawcaRepository.findAll();
    }
}