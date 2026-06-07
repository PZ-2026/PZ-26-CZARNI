package magazyn.service;

import magazyn.dto.*;
import magazyn.entity.*;
import magazyn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private DaneFinansoweRepository daneFinansoweRepository;

    @Autowired
    private KonfiguracijaRepository konfiguracijaRepository;

    @Autowired
    private ProduktRepository produktRepository;

    @Autowired
    private ZamowienieKlientaRepository zamowienieKlientaRepository;

    @Autowired
    private StanMagazynuRepository stanMagazynuRepository;

    @Autowired
    private DostawcaRepository dostawcaRepository;

    // ============ UŻYTKOWNICY (USER MANAGEMENT) ============

    /**
     * Pobierz wszystkich użytkowników
     */
    public List<UzytkownikAdminDTO> pobierzWszystkowUzytkownikow() {
        return uzytkownikRepository.findAll()
                .stream()
                .map(this::konwertujNaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pobierz użytkownika po ID
     */
    public Optional<UzytkownikAdminDTO> pobierzUzytkownika(Integer id) {
        return uzytkownikRepository.findById(id)
                .map(this::konwertujNaDTO);
    }

    /**
     * Utwórz nowego użytkownika (administrator)
     */
    @Transactional
    public UzytkownikAdminDTO utworzUzytkownika(UzytkownikAdminDTO dto) {
        if (uzytkownikRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Użytkownik z tym emailem już istnieje");
        }

        Uzytkownik uzytkownik = new Uzytkownik();
        uzytkownik.setImie(dto.getImie());
        uzytkownik.setNazwisko(dto.getNazwisko());
        uzytkownik.setEmail(dto.getEmail());
        uzytkownik.setTelefon(dto.getTelefon());
        uzytkownik.setRola(dto.getRola());
        uzytkownik.setFirma(dto.getFirma());
        uzytkownik.setNip(dto.getNip());
        uzytkownik.setZablokowany(false);
        
        // Domyślne hasło dla nowych użytkowników (powinno być zmienione)
        uzytkownik.setHaslo("DefaultPassword123!");

        Uzytkownik zapisany = uzytkownikRepository.save(uzytkownik);
        return konwertujNaDTO(zapisany);
    }

    /**
     * Edytuj użytkownika
     */
    @Transactional
    public UzytkownikAdminDTO edytujUzytkownika(Integer id, UzytkownikAdminDTO dto) {
        Uzytkownik uzytkownik = uzytkownikRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        uzytkownik.setImie(dto.getImie());
        uzytkownik.setNazwisko(dto.getNazwisko());
        uzytkownik.setTelefon(dto.getTelefon());
        uzytkownik.setRola(dto.getRola());
        uzytkownik.setFirma(dto.getFirma());
        
        // Bezpieczne ustawienie NIP - konwertuj pusty string na null
        String nip = dto.getNip();
        uzytkownik.setNip((nip == null || nip.trim().isEmpty()) ? null : nip.trim());

        if (!uzytkownik.getEmail().equals(dto.getEmail())) {
            if (uzytkownikRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Użytkownik z tym emailem już istnieje");
            }
            uzytkownik.setEmail(dto.getEmail());
        }

        // Nie zmieniamy hasła podczas edycji - hasło wymaga osobnego endpointa
        // Dlatego nie ustawiamy setHaslo() tutaj
        
        Uzytkownik zaktualizowany = uzytkownikRepository.saveAndFlush(uzytkownik);
        return konwertujNaDTO(zaktualizowany);
    }

    /**
     * Zablokuj użytkownika
     */
    @Transactional
    public void zablokowakUzytkownika(Integer id) {
        Uzytkownik uzytkownik = uzytkownikRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));
        uzytkownik.setZablokowany(true);
        uzytkownikRepository.save(uzytkownik);
    }

    /**
     * Odblokuj użytkownika
     */
    @Transactional
    public void odblokowakUzytkownika(Integer id) {
        Uzytkownik uzytkownik = uzytkownikRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));
        uzytkownik.setZablokowany(false);
        uzytkownikRepository.save(uzytkownik);
    }

    /**
     * Usuń użytkownika
     */
    @Transactional
    public void usunUzytkownika(Integer id) {
        if (!uzytkownikRepository.existsById(id)) {
            throw new IllegalArgumentException("Użytkownik nie znaleziony");
        }
        uzytkownikRepository.deleteById(id);
    }

    /**
     * Pobierz użytkowników po roli
     */
    public List<UzytkownikAdminDTO> pobierzUzytkownikoPORoli(Integer rola) {
        return uzytkownikRepository.findByRola(rola)
                .stream()
                .map(this::konwertujNaDTO)
                .collect(Collectors.toList());
    }

    // ============ ZARZĄDZANIE ROLAMI ============

    /**
     * Zmień rolę użytkownika
     */
    @Transactional
    public UzytkownikAdminDTO zmienRoleUzytkownika(Integer id, Integer nowaRola) {
        if (nowaRola < 1 || nowaRola > 4) {
            throw new IllegalArgumentException("Niepoprawna rola");
        }

        Uzytkownik uzytkownik = uzytkownikRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie znaleziony"));

        uzytkownik.setRola(nowaRola);
        Uzytkownik zaktualizowany = uzytkownikRepository.save(uzytkownik);
        return konwertujNaDTO(zaktualizowany);
    }

    /**
     * Pobierz statystyki użytkowników po rolach
     */
    public java.util.Map<String, Integer> pobierzStatystykiRol() {
        java.util.Map<String, Integer> statystyki = new java.util.HashMap<>();
        statystyki.put("administrator", uzytkownikRepository.countByRola(1));
        statystyki.put("magazynier", uzytkownikRepository.countByRola(2));
        statystyki.put("zaopatrzeniowiec", uzytkownikRepository.countByRola(3));
        statystyki.put("klient", uzytkownikRepository.countByRola(4));
        return statystyki;
    }

    // ============ DANE FINANSOWE ============

    /**
     * Pobierz raport finansowy za okres - na podstawie zamówień
     */
    public RaportFinansowyDTO pobierzRaportFinansowy(LocalDateTime dataPoczatek, LocalDateTime dataKoniec) {
        // Konwertuj LocalDateTime na OffsetDateTime
        OffsetDateTime poczatek = dataPoczatek.atOffset(ZoneOffset.UTC);
        OffsetDateTime koniec = dataKoniec.atOffset(ZoneOffset.UTC);
        
        // Przychody z zamówień
        BigDecimal sumaPrzychodow = zamowienieKlientaRepository.sumaPrzychodowZZamowien(poczatek, koniec);
        
        // Wydatki - na razie 0, można rozszerzyć o dostawy
        BigDecimal sumaWydatkow = BigDecimal.ZERO;
        
        // Zysk = Przychody - Wydatki
        BigDecimal sumaZysku = sumaPrzychodow.subtract(sumaWydatkow);

        // Konwertuj null na BigDecimal.ZERO
        sumaPrzychodow = sumaPrzychodow != null ? sumaPrzychodow : BigDecimal.ZERO;
        sumaWydatkow = sumaWydatkow != null ? sumaWydatkow : BigDecimal.ZERO;
        sumaZysku = sumaZysku != null ? sumaZysku : BigDecimal.ZERO;

        return new RaportFinansowyDTO(dataPoczatek, dataKoniec, sumaPrzychodow, sumaWydatkow, sumaZysku);
    }

    /**
     * Pobierz przychody za bieżący miesiąc
     */
    public BigDecimal pobierzPrzychodyMiesiac() {
        YearMonth biezacyMiesiac = YearMonth.now();
        LocalDateTime poczatek = biezacyMiesiac.atDay(1).atStartOfDay();
        LocalDateTime koniec = biezacyMiesiac.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal suma = daneFinansoweRepository.sumaPrzychodow(poczatek, koniec);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    /**
     * Pobierz wydatki za bieżący miesiąc
     */
    public BigDecimal pobierzWydatkiMiesiac() {
        YearMonth biezacyMiesiac = YearMonth.now();
        LocalDateTime poczatek = biezacyMiesiac.atDay(1).atStartOfDay();
        LocalDateTime koniec = biezacyMiesiac.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal suma = daneFinansoweRepository.sumaWydatkow(poczatek, koniec);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    /**
     * Pobierz zysk za bieżący miesiąc
     */
    public BigDecimal pobierzZyskMiesiac() {
        YearMonth biezacyMiesiac = YearMonth.now();
        LocalDateTime poczatek = biezacyMiesiac.atDay(1).atStartOfDay();
        LocalDateTime koniec = biezacyMiesiac.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal suma = daneFinansoweRepository.sumaZysku(poczatek, koniec);
        return suma != null ? suma : BigDecimal.ZERO;
    }

    /**
     * Pobierz historię finansową
     */
    public List<DaneFinansowe> pobierzHistorieFinansowa(LocalDateTime dataPoczatek, LocalDateTime dataKoniec) {
        return daneFinansoweRepository.findByDataBetweenOrderByDataDesc(dataPoczatek, dataKoniec);
    }

    /**
     * Dodaj nowy wpis finansowy
     */
    @Transactional
    public DaneFinansowe dodajWpisFinansowy(DaneFinansowe dane) {
        if (dane.getPrzychody() == null) {
            dane.setPrzychody(BigDecimal.ZERO);
        }
        if (dane.getWydatki() == null) {
            dane.setWydatki(BigDecimal.ZERO);
        }
        return daneFinansoweRepository.save(dane);
    }

    // ============ KONFIGURACJA SYSTEMU ============

    /**
     * Pobierz wszystkie parametry konfiguracyjne
     */
    public List<KonfiguracijaDTO> pobierzWszystkoKonfiguracje() {
        return konfiguracijaRepository.findAll()
                .stream()
                .map(this::konwertujKonfiguraceNaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pobierz aktywne parametry konfiguracyjne
     */
    public List<KonfiguracijaDTO> pobierzAktywneKonfiguracje() {
        return konfiguracijaRepository.findByAktywnaTrue()
                .stream()
                .map(this::konwertujKonfiguraceNaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pobierz parametr konfiguracyjny po nazwie
     */
    public Optional<KonfiguracijaDTO> pobierzKonfiguracje(String nazwaParametru) {
        return konfiguracijaRepository.findByNazwaParametru(nazwaParametru)
                .map(this::konwertujKonfiguraceNaDTO);
    }

    /**
     * Utwórz nowy parametr konfiguracyjny
     */
    @Transactional
    public KonfiguracijaDTO utworzKonfiguracje(KonfiguracijaDTO dto) {
        if (konfiguracijaRepository.findByNazwaParametru(dto.getNazwaParametru()).isPresent()) {
            throw new IllegalArgumentException("Parametr z tą nazwą już istnieje");
        }

        Konfiguracja konfiguracja = new Konfiguracja();
        konfiguracja.setNazwaParametru(dto.getNazwaParametru());
        konfiguracja.setWartoscParametru(dto.getWartoscParametru());
        konfiguracja.setTypParametru(dto.getTypParametru());
        konfiguracja.setOpis(dto.getOpis());
        konfiguracja.setAktywna(dto.getAktywna() != null ? dto.getAktywna() : true);

        Konfiguracja zapisana = konfiguracijaRepository.save(konfiguracja);
        return konwertujKonfiguraceNaDTO(zapisana);
    }

    /**
     * Edytuj parametr konfiguracyjny
     */
    @Transactional
    public KonfiguracijaDTO edytujKonfiguracje(Integer id, KonfiguracijaDTO dto) {
        Konfiguracja konfiguracja = konfiguracijaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Konfiguracja nie znaleziona"));

        konfiguracja.setWartoscParametru(dto.getWartoscParametru());
        konfiguracja.setTypParametru(dto.getTypParametru());
        konfiguracja.setOpis(dto.getOpis());
        konfiguracja.setAktywna(dto.getAktywna());

        Konfiguracja zaktualizowana = konfiguracijaRepository.save(konfiguracja);
        return konwertujKonfiguraceNaDTO(zaktualizowana);
    }

    /**
     * Usuń parametr konfiguracyjny
     */
    @Transactional
    public void usunKonfiguracje(Integer id) {
        if (!konfiguracijaRepository.existsById(id)) {
            throw new IllegalArgumentException("Konfiguracja nie znaleziona");
        }
        konfiguracijaRepository.deleteById(id);
    }

    // ============ DOSTAWCY ============

    /**
     * Pobierz wszystkich dostawców
     */
    public List<DostawcaDTO> pobierzWszystkichDostawcow() {
        return dostawcaRepository.findAll()
                .stream()
                .map(this::konwertujDostawceNaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pobierz dostawcę po ID
     */
    public Optional<DostawcaDTO> pobierzDostawce(Integer id) {
        return dostawcaRepository.findById(id)
                .map(this::konwertujDostawceNaDTO);
    }

    /**
     * Utwórz nowego dostawcę
     */
    @Transactional
    public DostawcaDTO utworzDostawce(DostawcaDTO dto) {
        Dostawca dostawca = new Dostawca();
        dostawca.setNazwaDostawcy(dto.getNazwaDostawcy());
        dostawca.setAdres(dto.getAdres());
        dostawca.setTelefon(dto.getTelefon());

        Dostawca zapisany = dostawcaRepository.save(dostawca);
        return konwertujDostawceNaDTO(zapisany);
    }

    /**
     * Edytuj dostawcę
     */
    @Transactional
    public DostawcaDTO edytujDostawce(Integer id, DostawcaDTO dto) {
        Dostawca dostawca = dostawcaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dostawca nie znaleziony"));

        dostawca.setNazwaDostawcy(dto.getNazwaDostawcy());
        dostawca.setAdres(dto.getAdres());
        dostawca.setTelefon(dto.getTelefon());

        Dostawca zaktualizowany = dostawcaRepository.save(dostawca);
        return konwertujDostawceNaDTO(zaktualizowany);
    }

    /**
     * Usuń dostawcę
     */
    @Transactional
    public void usunDostawce(Integer id) {
        if (!dostawcaRepository.existsById(id)) {
            throw new IllegalArgumentException("Dostawca nie znaleziony");
        }
        dostawcaRepository.deleteById(id);
    }

    // ============ STAN MAGAZYNU ============

    /**
     * Pobierz cały stan magazynu
     */
    public List<StanMagazynuDTO> pobierzCalyStanMagazynu() {
        return stanMagazynuRepository.findAll()
                .stream()
                .map(this::konwertujStanMagazynuNaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pobierz stan produktu
     */
    public Optional<StanMagazynuDTO> pobierzStanProduktu(Integer idProduktu) {
        return stanMagazynuRepository.findByProdukt_Id(idProduktu)
                .map(this::konwertujStanMagazynuNaDTO);
    }

    /**
     * Edytuj stan magazynu produktu
     */
    @Transactional
    public StanMagazynuDTO edytujStanMagazynu(Integer id, StanMagazynuDTO dto) {
        StanMagazynu stan = stanMagazynuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stan magazynu nie znaleziony"));

        stan.setIlosc(dto.getIlosc());

        StanMagazynu zaktualizowany = stanMagazynuRepository.save(stan);
        return konwertujStanMagazynuNaDTO(zaktualizowany);
    }

    /**
     * Utwórz nowy produkt wraz z wpisem w stanie magazynu
     */
    @Transactional
    public StanMagazynuDTO utworzStanMagazynu(StanMagazynuDTO dto) {
        if (dto.getIlosc() == null) {
            throw new IllegalArgumentException("Ilość jest wymagana");
        }

        Produkt produkt;
        if (dto.getIdProduktu() != null) {
            produkt = produktRepository.findById(dto.getIdProduktu())
                    .orElseThrow(() -> new IllegalArgumentException("Produkt o podanym ID nie istnieje"));
            if (stanMagazynuRepository.findByProdukt_Id(produkt.getId()).isPresent()) {
                throw new IllegalArgumentException("Stan magazynu dla tego produktu już istnieje");
            }
        } else {
            if (dto.getNazwaProduktu() == null || dto.getNazwaProduktu().isBlank()) {
                throw new IllegalArgumentException("Nazwa produktu jest wymagana");
            }
            if (dto.getCenaProduktu() == null) {
                throw new IllegalArgumentException("Cena produktu jest wymagana");
            }
            produkt = new Produkt();
            produkt.setNazwaProduktu(dto.getNazwaProduktu());
            produkt.setCena(dto.getCenaProduktu());
            produkt.setJednostka("szt.");
            produkt.setStrefa("strefa_A");
            produkt = produktRepository.save(produkt);
        }

        StanMagazynu stanMagazynu = new StanMagazynu();
        stanMagazynu.setProdukt(produkt);
        stanMagazynu.setIlosc(dto.getIlosc());
        StanMagazynu zapisanyStan = stanMagazynuRepository.save(stanMagazynu);
        return konwertujStanMagazynuNaDTO(zapisanyStan);
    }

    // ============ PANEL ADMINISTRATORA / DASHBOARD ============

    /**
     * Pobierz dane do panelu administratora (dashboard)
     */
    public PanelAdminaDTO pobierzDanePanelu() {
        PanelAdminaDTO panel = new PanelAdminaDTO();

        // Statystyki użytkowników
        panel.setLiczbaUzytkownikow(Math.toIntExact(uzytkownikRepository.count()));
        
        // Statystyki produktów
        panel.setLiczbaProduktu(Math.toIntExact(produktRepository.count()));

        // Dane finansowe za bieżący miesiąc
        panel.setPrzychodyMiesiac(pobierzPrzychodyMiesiac());
        panel.setWydatkiMiesiac(pobierzWydatkiMiesiac());
        panel.setZyskMiesiac(pobierzZyskMiesiac());

        // TODO: Dodać statystyki dotyczące zamówień i stanu magazynu
        // Te dane wymagają pełnego zrozumienia modelu danych

        return panel;
    }

    // ============ METODY POMOCNICZE ============

    /**
     * Konwertuj entitę Uzytkownik na DTO
     */
    private UzytkownikAdminDTO konwertujNaDTO(Uzytkownik uzytkownik) {
        UzytkownikAdminDTO dto = new UzytkownikAdminDTO();
        dto.setId(uzytkownik.getId());
        dto.setImie(uzytkownik.getImie());
        dto.setNazwisko(uzytkownik.getNazwisko());
        dto.setEmail(uzytkownik.getEmail());
        dto.setTelefon(uzytkownik.getTelefon());
        dto.setRola(uzytkownik.getRola());
        dto.setFirma(uzytkownik.getFirma());
        dto.setNip(uzytkownik.getNip());
        dto.setZablokowany(uzytkownik.getZablokowany());
        return dto;
    }

    /**
     * Konwertuj entitę Konfiguracja na DTO
     */
    private KonfiguracijaDTO konwertujKonfiguraceNaDTO(Konfiguracja konfiguracja) {
        KonfiguracijaDTO dto = new KonfiguracijaDTO();
        dto.setId(konfiguracja.getId());
        dto.setNazwaParametru(konfiguracja.getNazwaParametru());
        dto.setWartoscParametru(konfiguracja.getWartoscParametru());
        dto.setTypParametru(konfiguracja.getTypParametru());
        dto.setOpis(konfiguracja.getOpis());
        dto.setAktywna(konfiguracja.getAktywna());
        return dto;
    }

    /**
     * Konwertuj entitę Dostawca na DTO
     */
    private DostawcaDTO konwertujDostawceNaDTO(Dostawca dostawca) {
        DostawcaDTO dto = new DostawcaDTO();
        dto.setId(dostawca.getId());
        dto.setNazwaDostawcy(dostawca.getNazwaDostawcy());
        dto.setAdres(dostawca.getAdres());
        dto.setTelefon(dostawca.getTelefon());
        return dto;
    }

    /**
     * Konwertuj entitę StanMagazynu na DTO
     */
    private StanMagazynuDTO konwertujStanMagazynuNaDTO(StanMagazynu stan) {
        StanMagazynuDTO dto = new StanMagazynuDTO();
        dto.setId(stan.getId());
        dto.setIlosc(stan.getIlosc());
        if (stan.getProdukt() != null) {
            dto.setIdProduktu(stan.getProdukt().getId());
            dto.setNazwaProduktu(stan.getProdukt().getNazwaProduktu());
            dto.setCenaProduktu(stan.getProdukt().getCena());
        }
        return dto;
    }
}

