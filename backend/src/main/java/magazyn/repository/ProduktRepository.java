package magazyn.repository;

import magazyn.entity.Produkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProduktRepository extends JpaRepository<Produkt, Integer> {
    List<Produkt> findByIdDostawcy(Integer idDostawcy);
}