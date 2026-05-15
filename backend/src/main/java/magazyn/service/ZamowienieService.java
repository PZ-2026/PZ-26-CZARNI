package magazyn.service;

import magazyn.dto.NoweZamowienieRequest;
import magazyn.dto.PozycjaZamowieniaResponse;
import magazyn.entity.*;
import magazyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private ProduktRepository produktRepository;

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
    public List<PozycjaZamowieniaResponse> getPozycjeDlaZamowienia(Integer idZamowienia) {
        // 1. Pobierz pozycje z tabeli łączącej
        List<ZamowienieProduktyDostawcy> pozycje = pozycjeRepo.findByIdZamowienia(idZamowienia);

        // 2. Optymalizacja: Pobierz wszystkie ID produktów i wyciągnij je jednym zapytaniem
        List<Integer> ids = pozycje.stream()
                .map(ZamowienieProduktyDostawcy::getIdProduktu)
                .collect(Collectors.toList());

        // Jedno zapytanie zamiast wielu (SELECT * FROM produkty WHERE id IN (...))
        List<Produkt> produkty = produktRepository.findAllById(ids);

        // Mapujemy produkty na mapę dla szybkiego dostępu: ID -> Produkt
        Map<Integer, Produkt> produktMap = produkty.stream()
                .collect(Collectors.toMap(Produkt::getId, p -> p));

        // 3. Budowanie odpowiedzi z logowaniem (Dobre praktyki - Osoba 5)
        return pozycje.stream().map(p -> {
            Produkt prod = produktMap.get(p.getIdProduktu());
            String nazwa;

            if (prod != null) {
                nazwa = prod.getNazwaProduktu();
            } else {
                nazwa = "Nieznany produkt (ID: " + p.getIdProduktu() + ")";
            }

            return new PozycjaZamowieniaResponse(p.getIdProduktu(), nazwa, p.getIlosc());
        }).collect(Collectors.toList());
    }
}