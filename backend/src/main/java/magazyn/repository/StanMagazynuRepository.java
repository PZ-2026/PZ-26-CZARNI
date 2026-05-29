package magazyn.repository;

import magazyn.entity.StanMagazynu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repozytorium zarządzające stanami magazynowymi produktów.
 */
@Repository
public interface StanMagazynuRepository extends JpaRepository<StanMagazynu, Integer> {

    /**
     * Wyszukuje stan magazynowy powiązany z konkretnym produktem.
     *
     * @param idProduktu identyfikator produktu
     * @return opcjonalny obiekt stanu magazynowego
     */
    Optional<StanMagazynu> findByProdukt_Id(Integer idProduktu);
}