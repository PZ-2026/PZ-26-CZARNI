package magazyn.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NoweZamowienieRequest {
    private Integer idDostawcy;
    private Integer idUzytkownika;
    private List<PozycjaZamowienia> pozycje;

    @Getter
    @Setter
    public static class PozycjaZamowienia {
        private Integer idProduktu;
        private Integer ilosc;
    }
}