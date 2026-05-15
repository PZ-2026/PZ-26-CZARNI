package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PozycjaZamowieniaResponse {
    private Integer idProduktu;
    private String nazwaProduktu;
    private Integer ilosc;
}