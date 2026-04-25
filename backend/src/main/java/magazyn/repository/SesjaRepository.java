package magazyn.repository;

import magazyn.entity.Sesja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SesjaRepository extends JpaRepository<Sesja, Long> {
    Optional<Sesja> findByToken(String token);

}
