package magazyn.repository;

import magazyn.entity.Konfiguracja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface KonfiguracijaRepository extends JpaRepository<Konfiguracja, Integer> {

    Optional<Konfiguracja> findByNazwaParametru(String nazwaParametru);

    List<Konfiguracja> findByAktywnaTrue();

    List<Konfiguracja> findByTypParametru(String typParametru);
}
