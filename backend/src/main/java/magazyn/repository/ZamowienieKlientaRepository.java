package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Wyszukuje zamówienia przypisane do konkretnego magazyniera, o statusie innym niż podany.
* Wykorzystywane do pobierania listy zadań "do spakowania".
*
* @param magazynierId identyfikator magazyniera
* @param status status, który ma zostać wykluczony (zazwyczaj status 2 - skompletowane)
* @return lista zamówień spełniających kryteria
*/
@Repository
public interface ZamowienieKlientaRepository extends JpaRepository<ZamowienieKlienta, Integer> {

    List<ZamowienieKlienta> findByKlientId(Integer klientId);

    List<ZamowienieKlienta> findByStatus(Integer status);

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status != 4")
    Integer countZamowieniaWProgress();

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status = 1")
    Integer countZamowieniaDoRealizacji();
}
