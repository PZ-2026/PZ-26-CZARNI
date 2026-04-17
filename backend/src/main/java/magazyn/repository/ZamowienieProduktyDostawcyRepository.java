package magazyn.repository;

import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieProduktyDostawcyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZamowienieProduktyDostawcyRepository extends JpaRepository<ZamowienieProduktyDostawcy, ZamowienieProduktyDostawcyId> {
    List<ZamowienieProduktyDostawcy> findByIdZamowienia(Integer idZamowienia);
}