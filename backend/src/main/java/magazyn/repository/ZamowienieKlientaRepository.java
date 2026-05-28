package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium obsługujące operacje na zamówieniach klientów przypisanych do magazynierów.
 */
@Repository
public interface ZamowienieKlientaRepository extends JpaRepository<ZamowienieKlienta, Integer> {
    /**
     * Wyszukuje zamówienia przypisane do konkretnego magazyniera, o statusie innym niż podany.
     * Wykorzystywane do pobierania listy zadań "do spakowania".
     *
     * @param magazynierId identyfikator magazyniera
     * @param status status, który ma zostać wykluczony (zazwyczaj status 2 - skompletowane)
     * @return lista zamówień spełniających kryteria
     */
    List<ZamowienieKlienta> findByMagazynierIdAndStatusNot(Integer magazynierId, Integer status);
}
