package magazyn.service;

import jakarta.transaction.Transactional;
import magazyn.dto.NoweZamowienieRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Serwis obsługujący proces składania zamówień przez klientów.
 * Zarządza przydzielaniem zamówień do magazynierów oraz aktualizacją stanów magazynowych.
 */
@Service
public class ZamowienieKlientService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Składa nowe zamówienie klienta. Proces obejmuje:
     * 1. Znalezienie magazyniera z najmniejszą liczbą aktywnych zadań.
     * 2. Utworzenie nagłówka zamówienia.
     * 3. Walidację stanów magazynowych i dodanie pozycji zamówienia z "zamrożoną" ceną z dnia zakupu.
     *
     * @param request obiekt zawierający dane zamówienia (ID klienta, lista produktów z ilościami)
     * @throws RuntimeException jeśli brakuje towaru w magazynie
     */
    @Transactional
    public void zlozZamowienie(NoweZamowienieRequest request) {
        // 1. Znajdź magazyniera z najmniejszą ilością pracy
        Integer idWybranegoMagazyniera = null;
        try {
            String sqlFindWorker =
                    "SELECT u.id FROM uzytkownicy u " +
                    "LEFT JOIN zamowienia_klienci z ON u.id = z.id_magazyniera AND z.status != 2 " +
                    "WHERE u.rola = 1 " +
                    "GROUP BY u.id ORDER BY COUNT(z.id) ASC LIMIT 1";

            idWybranegoMagazyniera = jdbcTemplate.queryForObject(sqlFindWorker, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            idWybranegoMagazyniera = null;
        }

        // 2. Wstawianie głównego zamówienia
        final Integer finalMagazynierId = idWybranegoMagazyniera;
        String sqlZamowienie = "INSERT INTO zamowienia_klienci (id_klienta, id_magazyniera, data, status) " +
                "VALUES (?, ?, NOW(), 0) RETURNING id";

        Integer idNowegoZamowienia = jdbcTemplate.queryForObject(
                sqlZamowienie,
                Integer.class,
                request.getIdUzytkownika(),
                idWybranegoMagazyniera
        );

        System.out.println("SUKCES! Nowe ID zamówienia: " + idNowegoZamowienia);

        for (NoweZamowienieRequest.PozycjaZamowienia poz : request.getPozycje()) {
            // 1. Pobierz aktualną cenę produktu z bazy
            String sqlGetCena = "SELECT cena FROM produkty WHERE id = ?";
            Double aktualnaCena = jdbcTemplate.queryForObject(sqlGetCena, Double.class, poz.getIdProduktu());

            // 2. Zaktualizuj stan magazynowy
            String sqlUpdateStan = "UPDATE stan_magazynu SET ilosc = ilosc - ? WHERE id_produktu = ? AND ilosc >= ?";
            int zmienioneWiersze = jdbcTemplate.update(sqlUpdateStan, poz.getIlosc(), poz.getIdProduktu(), poz.getIlosc());

            if (zmienioneWiersze == 0) {
                throw new RuntimeException("Brak wystarczającej ilości produktu o ID: " + poz.getIdProduktu());
            }

            // 3. Wstaw produkt do zamówienia wraz z ceną "zamrożoną" w dniu zakupu
            String sqlInsertLaczaca = "INSERT INTO zamowienia_produkty_klienci_laczaca (id_zamowienia, id_produktu, ilosc, cena_w_dniu_zakupu) VALUES (?, ?, ?, ?)";

            jdbcTemplate.update(sqlInsertLaczaca,
                    idNowegoZamowienia,
                    poz.getIdProduktu(),
                    poz.getIlosc(),
                    aktualnaCena
            );
        }
    }
}
