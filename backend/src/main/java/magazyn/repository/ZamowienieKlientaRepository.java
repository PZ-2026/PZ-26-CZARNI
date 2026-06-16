package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

    List<ZamowienieKlienta> findByMagazynierIdAndStatusNot(Integer magazynierId, Integer status);

    List<ZamowienieKlienta> findByDataBetween(OffsetDateTime dataPoczatek, OffsetDateTime dataKoniec);

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status != 4")
    Integer countZamowieniaWProgress();

    @Query("SELECT COUNT(z) FROM ZamowienieKlienta z WHERE z.status = 1")
    Integer countZamowieniaDoRealizacji();

    @Query("SELECT COALESCE(SUM(z.ilosc * z.cenaWDniuZakupu), 0) FROM ZamowienieProduktyKlienci z JOIN z.zamowienie zk WHERE zk.data >= ?1 AND zk.data <= ?2")
    BigDecimal sumaPrzychodowZZamowien(OffsetDateTime dataPoczatek, OffsetDateTime dataKoniec);
}
