package magazyn.repository;

import magazyn.entity.DaneFinansowe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DaneFinansoweRepository extends JpaRepository<DaneFinansowe, Integer> {

    List<DaneFinansowe> findByTyp(String typ);

    List<DaneFinansowe> findByDataBetweenOrderByDataDesc(LocalDateTime dataPoczatek, LocalDateTime dataKoniec);

    @Query("SELECT SUM(d.przychody) FROM DaneFinansowe d WHERE d.data BETWEEN ?1 AND ?2")
    BigDecimal sumaPrzychodow(LocalDateTime dataPoczatek, LocalDateTime dataKoniec);

    @Query("SELECT SUM(d.wydatki) FROM DaneFinansowe d WHERE d.data BETWEEN ?1 AND ?2")
    BigDecimal sumaWydatkow(LocalDateTime dataPoczatek, LocalDateTime dataKoniec);

    @Query("SELECT SUM(d.zysk) FROM DaneFinansowe d WHERE d.data BETWEEN ?1 AND ?2")
    BigDecimal sumaZysku(LocalDateTime dataPoczatek, LocalDateTime dataKoniec);

    List<DaneFinansowe> findByTypAndDataBetweenOrderByDataDesc(String typ, LocalDateTime dataPoczatek, LocalDateTime dataKoniec);
}
