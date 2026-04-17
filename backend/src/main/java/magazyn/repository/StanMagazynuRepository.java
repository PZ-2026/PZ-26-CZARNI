package magazyn.repository;

import magazyn.entity.StanMagazynu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StanMagazynuRepository extends JpaRepository<StanMagazynu, Integer> {

    // Pozwala znaleźć stan magazynowy na podstawie ID produktu
    Optional<StanMagazynu> findByProdukt_Id(Integer idProduktu);
}