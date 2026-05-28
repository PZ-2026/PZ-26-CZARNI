package magazyn.repository;

import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieProduktyDostawcyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium zarządzające pozycjami zamówień zaopatrzeniowych.
 * Obsługuje powiązania między zamówieniami zaopatrzeniowców a produktami.
 */
@Repository
public interface ZamowienieProduktyDostawcyRepository extends JpaRepository<ZamowienieProduktyDostawcy, ZamowienieProduktyDostawcyId> {
    /**
     * Pobiera listę pozycji przypisanych do konkretnego zamówienia zaopatrzeniowego.
     *
     * @param idZamowienia identyfikator zamówienia zaopatrzeniowego
     * @return lista pozycji zamówienia
     */
    List<ZamowienieProduktyDostawcy> findByIdZamowienia(Integer idZamowienia);
}