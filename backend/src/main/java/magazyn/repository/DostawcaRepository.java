package magazyn.repository;

import magazyn.entity.Dostawca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium zarządzające encjami dostawców.
 */
@Repository
public interface DostawcaRepository extends JpaRepository<Dostawca, Integer> {
}