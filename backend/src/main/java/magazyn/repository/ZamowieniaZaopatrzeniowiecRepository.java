package magazyn.repository;

import magazyn.dto.HistoriaZamowieniaDTO;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Repozytorium obsługujące zamówienia zaopatrzeniowe.
 */
@Repository
public interface ZamowieniaZaopatrzeniowiecRepository extends JpaRepository<ZamowienieZaopatrzeniowca, Integer> {

    /**
     * Pobiera historię zamówień zaopatrzeniowych dla konkretnego użytkownika.
     * Agreguje dane o dostawcy i liczbie pozycji w zamówieniu.
     *
     * @param idUzytkownika identyfikator zaopatrzeniowca
     * @return lista obiektów DTO reprezentujących historię zamówień
     */
    @Query("SELECT new magazyn.dto.HistoriaZamowieniaDTO(z.id, z.data, d.nazwaDostawcy, z.status, COUNT(l)) " +
            "FROM ZamowienieZaopatrzeniowca z " +
            "JOIN z.dostawca d " +
            "LEFT JOIN ZamowienieProduktyDostawcy l ON z.id = l.idZamowienia " +
            "WHERE z.uzytkownik.id = :idUzytkownika " +
            "GROUP BY z.id, z.data, d.nazwaDostawcy, z.status " +
            "ORDER BY z.data DESC")
    List<HistoriaZamowieniaDTO> findHistoriaByUzytkownik(@Param("idUzytkownika") Integer idUzytkownika);

    /**
     * Pobiera sumę kosztów zamówień zaopatrzeniowych w danym okresie.
     * Oblicza koszt na podstawie ilości produktów i ich ceny katalogowej.
     *
     * @param dataPoczatek data początkowa okresu
     * @param dataKoniec data końcowa okresu
     * @return suma kosztów zamówień, lub null jeśli brak zamówień
     */
    @Query("SELECT COALESCE(SUM(l.ilosc * p.cena), 0) FROM ZamowienieZaopatrzeniowca z " +
            "JOIN ZamowienieProduktyDostawcy l ON z.id = l.idZamowienia " +
            "JOIN Produkt p ON l.idProduktu = p.id " +
            "WHERE z.data >= ?1 AND z.data <= ?2")
    BigDecimal sumaKosztowZamowien(OffsetDateTime dataPoczatek, OffsetDateTime dataKoniec);
}
