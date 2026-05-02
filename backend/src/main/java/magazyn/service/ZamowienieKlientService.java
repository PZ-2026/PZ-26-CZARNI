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

@Service
public class ZamowienieKlientService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void zlozZamowienie(NoweZamowienieRequest request) {
        // 1. Znajdź magazyniera z najmniejszą ilością pracy
        Integer idWybranegoMagazyniera = null;
        try {
            String sqlFindWorker =
                    "SELECT u.id FROM uzytkownicy u " +
                    "LEFT JOIN zamowienie_klienci z ON u.id = z.id_magazyniera AND z.status != 2 " +
                    "WHERE u.rola = 1 " +
                    "GROUP BY u.id ORDER BY COUNT(z.id) ASC LIMIT 1";

            idWybranegoMagazyniera = jdbcTemplate.queryForObject(sqlFindWorker, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            idWybranegoMagazyniera = null;
        }

        // 2. Wstawianie głównego zamówienia
        final Integer finalMagazynierId = idWybranegoMagazyniera;
        String sqlZamowienie = "INSERT INTO zamowienia_klienci (id_uzytkownika, id_magazyniera, data_zamowienia, status) VALUES (?, ?, NOW(), 0)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlZamowienie, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, request.getIdUzytkownika());
            if (finalMagazynierId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, finalMagazynierId);
            }
            return ps;
        }, keyHolder);

        int idNowegoZamowienia = keyHolder.getKey().intValue();

        for (NoweZamowienieRequest.PozycjaZamowienia poz: request.getPozycje()) {
            int zmienioneWiersze = jdbcTemplate.update("UPDATE stan_magazynu SET ilosc = ilosc - ? WHERE id_produktu = ? AND ilosc >= ?", poz.getIlosc(), poz.getIdProduktu(), poz.getIlosc());
            if (zmienioneWiersze == 0) {
                throw new RuntimeException("Brak wystarczającej ilości produktu o ID: " + poz.getIdProduktu());
            }
            jdbcTemplate.update("INSERT INTO zamowienia_produkty_klienci_laczaca (id_zamowienia, id_produktu, ilosc) VALUES (?, ?, ?)", idNowegoZamowienia, poz.getIdProduktu(), poz.getIlosc());

        }
    }
}
