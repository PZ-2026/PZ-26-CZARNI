package magazyn.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StanMagazynuDTO {

    private Integer id;

    @NotNull(message = "Ilość jest wymagana")
    private BigDecimal ilosc;

    @NotNull(message = "ID produktu jest wymagane")
    private Integer idProduktu;

    private String nazwaProduktu;

    private BigDecimal cenaProduktu;
}
