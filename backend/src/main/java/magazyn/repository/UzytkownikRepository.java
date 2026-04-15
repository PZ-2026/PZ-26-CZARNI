package magazyn.repository;

import magazyn.entity.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Integer> {
    // Spring sam wygeneruje zapytanie na podstawie nazwy metody!
    Optional<Uzytkownik> findByEmail(String email);
}