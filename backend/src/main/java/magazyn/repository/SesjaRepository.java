package magazyn.repository;

import magazyn.entity.Sesja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repozytorium zarządzające encjami sesji użytkowników.
 */
public interface SesjaRepository extends JpaRepository<Sesja, Long> {
    /**
     * Wyszukuje sesję na podstawie unikalnego tokenu.
     *
     * @param token token sesji
     * @return opcjonalny obiekt sesji
     */
    Optional<Sesja> findByToken(String token);

}
