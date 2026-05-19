package magazyn.repository;

import magazyn.entity.ZamowienieKlienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZamowienieKlientaRepository extends JpaRepository<ZamowienieKlienta, Integer> {
    List<ZamowienieKlienta> findByMagazynierIdAndStatusNot(Integer magazynierId, Integer status);
}
