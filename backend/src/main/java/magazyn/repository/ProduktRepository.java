package magazyn.repository;

import magazyn.entity.Produkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repozytorium zarządzające encjami produktów.
 */
@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Integer> {
    /**
     * Pobiera listę produktów przypisanych do określonego dostawcy.
     *
     * @param idDostawcy identyfikator dostawcy
     * @return lista produktów
     */
    List<Produkt> findByIdDostawcy(Integer idDostawcy);
}