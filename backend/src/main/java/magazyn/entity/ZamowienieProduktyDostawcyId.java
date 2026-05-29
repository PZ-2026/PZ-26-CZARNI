package magazyn.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

/**
 * Klasa reprezentująca klucz złożony dla tabeli łączącej produkty z zamówieniami zaopatrzeniowymi.
 */
@EqualsAndHashCode
public class ZamowienieProduktyDostawcyId implements Serializable {
    private Integer idProduktu;
    private Integer idZamowienia;
}
