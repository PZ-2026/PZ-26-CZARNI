package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repozytorium zarządzające encjami zamówień klientów.
 */
public interface ZamowienieRepository extends JpaRepository<ZamowienieKlienta, Long> {

    /**
     * Wyszukuje zamówienie klienta wraz z jego pozycjami i danymi produktów za pomocą jednego zapytania (Eager Loading).
     *
     * @param id identyfikator zamówienia
     * @return opcjonalny obiekt zamówienia z dociągniętymi relacjami
     */
    @Query("SELECT z FROM ZamowienieKlienta z " +
            "LEFT JOIN FETCH z.pozycje pl " +
            "LEFT JOIN FETCH pl.produkt " +
            "WHERE z.id = :id")
    Optional<ZamowienieKlienta> findByIdWithProducts(@Param("id") Integer id);
}
