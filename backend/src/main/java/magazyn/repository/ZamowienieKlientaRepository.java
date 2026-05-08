package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZamowienieKlientaRepository extends JpaRepository<ZamowienieKlienta, Integer> {

    List<ZamowienieKlienta> findByKlientId(Integer klientId);

    List<ZamowienieKlienta> findByStatus(Integer status);

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status != 4")
    Integer countZamowieniaWProgress();

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status = 1")
    Integer countZamowieniaDoRealizacji();
}
