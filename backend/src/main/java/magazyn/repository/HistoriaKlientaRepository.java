package magazyn.repository;

import magazyn.dto.HistoriaZamowieniaDTO;
import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repozytorium obsługujące pobieranie historii zamówień klienta.
 * Wykorzystuje zaawansowane zapytania JPQL do agregacji danych i mapowania ich na obiekty DTO.
 */
public interface HistoriaKlientaRepository extends JpaRepository<ZamowienieKlienta, Integer> {
    /**
     * Pobiera uproszczoną historię zamówień dla konkretnego klienta.
     * Oblicza sumaryczną ilość produktów oraz łączną kwotę brutto zamówienia.
     *
     * @param idKlienta identyfikator klienta
     * @return lista obiektów DTO z historią zamówień
     */
    @Query("SELECT new magazyn.dto.HistoriaZamowieniaDTO(" +
            "z.id, " +
            "z.data, " +
            "'Magazyn Główny', " +
            "z.status, " +
            "SUM(l.ilosc), " +
            "CAST(SUM(l.ilosc * l.cenaWDniuZakupu) * 1.23 AS float)) " +
            "FROM ZamowienieKlienta z " +
            "LEFT JOIN ZamowienieProduktyKlienci l ON l.zamowienie.id = z.id " +
            "WHERE z.klient.id = :idKlienta " +
            "GROUP BY z.id, z.data, z.status " +
            "ORDER BY z.data DESC")
    List<HistoriaZamowieniaDTO> findHistoriaKlienta(@Param("idKlienta") Integer idKlienta);
}
