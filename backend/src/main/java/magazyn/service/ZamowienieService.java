package magazyn.service;

import magazyn.dto.NoweZamowienieRequest;
import magazyn.entity.*;
import magazyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class ZamowienieService {

    @Autowired
    private ZamowieniaZaopatrzeniowiecRepository zamowieniaRepo;
    @Autowired
    private ZamowienieProduktyDostawcyRepository pozycjeRepo;
    @Autowired
    private DostawcaRepository dostawcaRepo;
    @Autowired
    private UzytkownikRepository uzytkownikRepo;

    @Transactional
    public void zlozZamowienie(NoweZamowienieRequest request) {
        // tworzenie nowego zamówienie
        ZamowienieZaopatrzeniowca zamowienie = new ZamowienieZaopatrzeniowca();
        zamowienie.setData(OffsetDateTime.now());
        zamowienie.setStatus(1); // 1 = W trakcie / Oczekujące

        zamowienie.setDostawca(dostawcaRepo.getReferenceById(request.getIdDostawcy()));
        zamowienie.setUzytkownik(uzytkownikRepo.getReferenceById(request.getIdUzytkownika()));

        // zapis zamówienia, by uzyskać jego ID
        ZamowienieZaopatrzeniowca zapisaneZamowienie = zamowieniaRepo.save(zamowienie);

        // zapis pozycji w tabeli łączącej
        for (NoweZamowienieRequest.PozycjaZamowienia pozycja : request.getPozycje()) {
            if (pozycja.getIlosc() > 0) {
                ZamowienieProduktyDostawcy element = new ZamowienieProduktyDostawcy();
                element.setIdZamowienia(zapisaneZamowienie.getId());
                element.setIdProduktu(pozycja.getIdProduktu());
                element.setIlosc(pozycja.getIlosc());
                pozycjeRepo.save(element);
            }
        }
    }
}