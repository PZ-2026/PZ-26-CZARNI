package magazyn.controllers;

import jakarta.validation.Valid;
import magazyn.dto.*;
import magazyn.entity.DaneFinansowe;
import magazyn.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ============ ZARZĄDZANIE UŻYTKOWNIKAMI ============

    /**
     * GET /api/admin/users - Pobierz wszystkich użytkowników
     */
    @GetMapping("/users")
    public ResponseEntity<List<UzytkownikAdminDTO>> pobierzWszystkowUzytkownikow() {
        try {
            List<UzytkownikAdminDTO> uzytkownicy = adminService.pobierzWszystkowUzytkownikow();
            return ResponseEntity.ok(uzytkownicy);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * GET /api/admin/users/{id} - Pobierz użytkownika po ID
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> pobierzUzytkownika(@PathVariable Integer id) {
        try {
            return adminService.pobierzUzytkownika(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/users - Utwórz nowego użytkownika
     */
    @PostMapping("/users")
    public ResponseEntity<?> utworzUzytkownika(@Valid @RequestBody UzytkownikAdminDTO dto) {
        try {
            UzytkownikAdminDTO nowyUzytkownik = adminService.utworzUzytkownika(dto);
            return ResponseEntity.ok(nowyUzytkownik);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas tworzenia użytkownika: " + e.getMessage());
        }
    }

    /**
     * PUT /api/admin/users/{id} - Edytuj użytkownika
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> edytujUzytkownika(@PathVariable Integer id, @Valid @RequestBody UzytkownikAdminDTO dto) {
        try {
            UzytkownikAdminDTO zaktualizowany = adminService.edytujUzytkownika(id, dto);
            return ResponseEntity.ok(zaktualizowany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas edycji użytkownika: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/admin/users/{id} - Usuń użytkownika
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> usunUzytkownika(@PathVariable Integer id) {
        try {
            adminService.usunUzytkownika(id);
            return ResponseEntity.ok("Użytkownik został usunięty");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas usuwania użytkownika: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/users/{id}/block - Zablokuj użytkownika
     */
    @PostMapping("/users/{id}/block")
    public ResponseEntity<?> zablokowakUzytkownika(@PathVariable Integer id) {
        try {
            adminService.zablokowakUzytkownika(id);
            return ResponseEntity.ok("Użytkownik został zablokowany");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/users/{id}/unblock - Odblokuj użytkownika
     */
    @PostMapping("/users/{id}/unblock")
    public ResponseEntity<?> odblokowakUzytkownika(@PathVariable Integer id) {
        try {
            adminService.odblokowakUzytkownika(id);
            return ResponseEntity.ok("Użytkownik został odblokowany");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/users/role/{roleId} - Pobierz użytkowników po roli
     */
    @GetMapping("/users/role/{roleId}")
    public ResponseEntity<List<UzytkownikAdminDTO>> pobierzUzytkownikowPoRoli(@PathVariable Integer roleId) {
        try {
            List<UzytkownikAdminDTO> uzytkownicy = adminService.pobierzUzytkownikoPORoli(roleId);
            return ResponseEntity.ok(uzytkownicy);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ============ ZARZĄDZANIE ROLAMI ============

    /**
     * PUT /api/admin/users/{id}/role/{newRoleId} - Zmień rolę użytkownika
     */
    @PutMapping("/users/{id}/role/{newRoleId}")
    public ResponseEntity<?> zmienRoleUzytkownika(@PathVariable Integer id, @PathVariable Integer newRoleId) {
        try {
            UzytkownikAdminDTO zaktualizowany = adminService.zmienRoleUzytkownika(id, newRoleId);
            return ResponseEntity.ok(zaktualizowany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/roles/statistics - Pobierz statystyki użytkowników po rolach
     */
    @GetMapping("/roles/statistics")
    public ResponseEntity<Map<String, Integer>> pobierzStatystykiRol() {
        try {
            Map<String, Integer> statystyki = adminService.pobierzStatystykiRol();
            return ResponseEntity.ok(statystyki);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // ============ DANE FINANSOWE ============

    /**
     * GET /api/admin/financial/report - Pobierz raport finansowy za okres
     */
    @GetMapping("/financial/report")
    public ResponseEntity<?> pobierzRaportFinansowy(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataPoczatek,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataKoniec) {
        try {
            YearMonth currentMonth = YearMonth.now();
            LocalDateTime poczatek = dataPoczatek != null ? dataPoczatek : currentMonth.atDay(1).atStartOfDay();
            LocalDateTime koniec = dataKoniec != null ? dataKoniec : currentMonth.atEndOfMonth().atTime(23, 59, 59);
            RaportFinansowyDTO raport = adminService.pobierzRaportFinansowy(poczatek, koniec);
            return ResponseEntity.ok(raport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/financial/revenue-month - Pobierz przychody za bieżący miesiąc
     */
    @GetMapping("/financial/revenue-month")
    public ResponseEntity<?> pobierzPrzychodyMiesiac() {
        try {
            var przychody = adminService.pobierzPrzychodyMiesiac();
            return ResponseEntity.ok(Map.of("przychody", przychody));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/financial/expenses-month - Pobierz wydatki za bieżący miesiąc
     */
    @GetMapping("/financial/expenses-month")
    public ResponseEntity<?> pobierzWydatkiMiesiac() {
        try {
            var wydatki = adminService.pobierzWydatkiMiesiac();
            return ResponseEntity.ok(Map.of("wydatki", wydatki));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/financial/profit-month - Pobierz zysk za bieżący miesiąc
     */
    @GetMapping("/financial/profit-month")
    public ResponseEntity<?> pobierzZyskMiesiac() {
        try {
            var zysk = adminService.pobierzZyskMiesiac();
            return ResponseEntity.ok(Map.of("zysk", zysk));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/financial/history - Pobierz historię finansową
     */
    @GetMapping("/financial/history")
    public ResponseEntity<?> pobierzHistorieFinansowa(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataPoczatek,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataKoniec) {
        try {
            List<DaneFinansowe> historia = adminService.pobierzHistorieFinansowa(dataPoczatek, dataKoniec);
            return ResponseEntity.ok(historia);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/financial/entry - Dodaj nowy wpis finansowy
     */
    @PostMapping("/financial/entry")
    public ResponseEntity<?> dodajWpisFinansowy(@RequestBody DaneFinansowe dane) {
        try {
            DaneFinansowe zapisane = adminService.dodajWpisFinansowy(dane);
            return ResponseEntity.ok(zapisane);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    // ============ KONFIGURACJA SYSTEMU ============

    /**
     * GET /api/admin/configuration - Pobierz wszystkie parametry konfiguracyjne
     */
    @GetMapping("/configuration")
    public ResponseEntity<List<KonfiguracijaDTO>> pobierzWszystkoKonfiguracje() {
        try {
            List<KonfiguracijaDTO> konfiguracje = adminService.pobierzWszystkoKonfiguracje();
            return ResponseEntity.ok(konfiguracje);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * GET /api/admin/configuration/active - Pobierz aktywne parametry konfiguracyjne
     */
    @GetMapping("/configuration/active")
    public ResponseEntity<List<KonfiguracijaDTO>> pobierzAktywneKonfiguracje() {
        try {
            List<KonfiguracijaDTO> konfiguracje = adminService.pobierzAktywneKonfiguracje();
            return ResponseEntity.ok(konfiguracje);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * GET /api/admin/configuration/{parametr} - Pobierz parametr konfiguracyjny po nazwie
     */
    @GetMapping("/configuration/{parametr}")
    public ResponseEntity<?> pobierzKonfiguracje(@PathVariable String parametr) {
        try {
            return adminService.pobierzKonfiguracje(parametr)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/configuration - Utwórz nowy parametr konfiguracyjny
     */
    @PostMapping("/configuration")
    public ResponseEntity<?> utworzKonfiguracje(@Valid @RequestBody KonfiguracijaDTO dto) {
        try {
            KonfiguracijaDTO nowaKonfiguracja = adminService.utworzKonfiguracje(dto);
            return ResponseEntity.ok(nowaKonfiguracja);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * PUT /api/admin/configuration/{id} - Edytuj parametr konfiguracyjny
     */
    @PutMapping("/configuration/{id}")
    public ResponseEntity<?> edytujKonfiguracje(@PathVariable Integer id, @Valid @RequestBody KonfiguracijaDTO dto) {
        try {
            KonfiguracijaDTO zaktualizowana = adminService.edytujKonfiguracje(id, dto);
            return ResponseEntity.ok(zaktualizowana);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/admin/configuration/{id} - Usuń parametr konfiguracyjny
     */
    @DeleteMapping("/configuration/{id}")
    public ResponseEntity<?> usunKonfiguracje(@PathVariable Integer id) {
        try {
            adminService.usunKonfiguracje(id);
            return ResponseEntity.ok("Konfiguracja została usunięta");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    // ============ DOSTAWCY ============

    /**
     * GET /api/admin/dostawcy - Pobierz wszystkich dostawców
     */
    @GetMapping("/dostawcy")
    public ResponseEntity<?> pobierzWszystkichDostawcow() {
        try {
            List<DostawcaDTO> dostawcy = adminService.pobierzWszystkichDostawcow();
            return ResponseEntity.ok(dostawcy);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/dostawcy/{id} - Pobierz dostawcę po ID
     */
    @GetMapping("/dostawcy/{id}")
    public ResponseEntity<?> pobierzDostawce(@PathVariable Integer id) {
        try {
            return adminService.pobierzDostawce(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/dostawcy - Utwórz nowego dostawcę
     */
    @PostMapping("/dostawcy")
    public ResponseEntity<?> utworzDostawce(@Valid @RequestBody DostawcaDTO dto) {
        try {
            DostawcaDTO nowyDostawca = adminService.utworzDostawce(dto);
            return ResponseEntity.ok(nowyDostawca);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * PUT /api/admin/dostawcy/{id} - Edytuj dostawcę
     */
    @PutMapping("/dostawcy/{id}")
    public ResponseEntity<?> edytujDostawce(@PathVariable Integer id, @Valid @RequestBody DostawcaDTO dto) {
        try {
            DostawcaDTO zaktualizowany = adminService.edytujDostawce(id, dto);
            return ResponseEntity.ok(zaktualizowany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/admin/dostawcy/{id} - Usuń dostawcę
     */
    @DeleteMapping("/dostawcy/{id}")
    public ResponseEntity<?> usunDostawce(@PathVariable Integer id) {
        try {
            adminService.usunDostawce(id);
            return ResponseEntity.ok("Dostawca został usunięty");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    // ============ STAN MAGAZYNU ============

    /**
     * GET /api/admin/magazyn - Pobierz cały stan magazynu
     */
    @GetMapping("/magazyn")
    public ResponseEntity<?> pobierzCalyStanMagazynu() {
        try {
            List<StanMagazynuDTO> stan = adminService.pobierzCalyStanMagazynu();
            return ResponseEntity.ok(stan);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * POST /api/admin/magazyn - Utwórz nowy produkt w magazynie
     */
    @PostMapping("/magazyn")
    public ResponseEntity<?> utworzStanMagazynu(@Valid @RequestBody StanMagazynuDTO dto) {
        try {
            StanMagazynuDTO nowyStan = adminService.utworzStanMagazynu(dto);
            return ResponseEntity.ok(nowyStan);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * GET /api/admin/magazyn/{idProduktu} - Pobierz stan produktu
     */
    @GetMapping("/magazyn/produkt/{idProduktu}")
    public ResponseEntity<?> pobierzStanProduktu(@PathVariable Integer idProduktu) {
        try {
            return adminService.pobierzStanProduktu(idProduktu)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    /**
     * PUT /api/admin/magazyn/{id} - Edytuj stan magazynu
     */
    @PutMapping("/magazyn/{id}")
    public ResponseEntity<?> edytujStanMagazynu(@PathVariable Integer id, @Valid @RequestBody StanMagazynuDTO dto) {
        try {
            StanMagazynuDTO zaktualizowany = adminService.edytujStanMagazynu(id, dto);
            return ResponseEntity.ok(zaktualizowany);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Błąd: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }

    // ============ PANEL ADMINISTRATORA / DASHBOARD ============

    /**
     * GET /api/admin/dashboard - Pobierz dane do panelu administratora
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> pobierzDanePanelu() {
        try {
            PanelAdminaDTO dane = adminService.pobierzDanePanelu();
            return ResponseEntity.ok(dane);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd: " + e.getMessage());
        }
    }
}

