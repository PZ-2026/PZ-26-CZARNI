package magazyn.repository;

import magazyn.dto.HistoriaZamowieniaDTO;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZamowieniaZaopatrzeniowiecRepository extends JpaRepository<ZamowienieZaopatrzeniowca, Integer> {

    @Query("SELECT new magazyn.dto.HistoriaZamowieniaDTO(z.id, z.data, d.nazwaDostawcy, z.status, COUNT(l)) " +
            "FROM ZamowienieZaopatrzeniowca z " +
            "JOIN z.dostawca d " +
            "LEFT JOIN ZamowienieProduktyDostawcy l ON z.id = l.idZamowienia " +
            "WHERE z.uzytkownik.id = :idUzytkownika " +
            "GROUP BY z.id, z.data, d.nazwaDostawcy, z.status " +
            "ORDER BY z.data DESC")
    List<HistoriaZamowieniaDTO> findHistoriaByUzytkownik(@Param("idUzytkownika") Integer idUzytkownika);
}
