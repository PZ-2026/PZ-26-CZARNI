package magazyn.repository;

import magazyn.entity.ZamowienieProduktyKlienci;
import magazyn.entity.ZamowienieProduktyKlienciId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium zarządzające pozycjami zamówień klientów.
 * Obsługuje tabelę łączącą zamówienia z produktami.
 */
@Repository
public interface ZamowienieProduktyKlienciRepository extends JpaRepository<ZamowienieProduktyKlienci, ZamowienieProduktyKlienciId> {
    /**
     * Pobiera listę pozycji przypisanych do konkretnego zamówienia klienta.
     *
     * @param zamowienieId identyfikator zamówienia
     * @return lista pozycji zamówienia
     */
    List<ZamowienieProduktyKlienci> findByZamowienieId(Integer zamowienieId);
}
