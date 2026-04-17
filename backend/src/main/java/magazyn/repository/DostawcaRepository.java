package magazyn.repository;

import magazyn.entity.Dostawca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DostawcaRepository extends JpaRepository<Dostawca, Integer> {
}