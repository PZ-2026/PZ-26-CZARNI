package magazyn.repository;

import magazyn.entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repozytorium zarządzające encjami użytkowników.
 */
@Repository
public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Integer> {
    /**
     * Wyszukuje użytkownika na podstawie adresu email.
     *
     * @param email adres email użytkownika
     * @return opcjonalny obiekt użytkownika
     */
    Optional<Uzytkownik> findByEmail(String email);
}