package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Obiekt DTO zawierający szczegółowe informacje o pozycji zamówienia.
 * Wykorzystywany do wyświetlania listy produktów w widoku szczegółów zamówienia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PozycjaZamowieniaResponse {
    private Integer idProduktu;
    private String nazwaProduktu;
    private Integer ilosc;
}
