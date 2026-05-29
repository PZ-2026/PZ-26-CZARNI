package magazyn.repository;

import magazyn.entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

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

    List<Uzytkownik> findByRola(Integer rola);

    List<Uzytkownik> findByZablokowanyTrue();

    List<Uzytkownik> findByZablokowanyFalse();

    @Query("SELECT COUNT(u) FROM Uzytkownik u WHERE u.zablokowany = false")
    Integer countAktywni();

    @Query("SELECT COUNT(u) FROM Uzytkownik u WHERE u.rola = ?1")
    Integer countByRola(Integer rola);
}