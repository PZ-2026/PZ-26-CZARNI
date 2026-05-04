package magazyn.repository;

import magazyn.entity.ZamowienieProduktyKlienci;
import magazyn.entity.ZamowienieProduktyKlienciId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZamowienieProduktyKlienciRepository extends JpaRepository<ZamowienieProduktyKlienci, ZamowienieProduktyKlienciId> {
    List<ZamowienieProduktyKlienci> findByZamowienieId(Integer zamowienieId);
}
