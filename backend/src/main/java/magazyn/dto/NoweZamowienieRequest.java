package magazyn.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Obiekt żądania wykorzystywany przy składaniu nowych zamówień.
 * Zawiera informacje o dostawcy (dla zaopatrzenia) lub kliencie oraz listę zamawianych produktów.
 */
@Getter
@Setter
public class NoweZamowienieRequest {
    private Integer idDostawcy;
    private Integer idUzytkownika;
    private List<PozycjaZamowienia> pozycje;

    /**
     * Wewnętrzna klasa reprezentująca pojedynczą pozycję w żądaniu zamówienia.
     */
    @Getter
    @Setter
    public static class PozycjaZamowienia {
        private Integer idProduktu;
        private Integer ilosc;
    }
}
