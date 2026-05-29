package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PanelAdminaDTO {

    private Integer liczbaUzytkownikow;
    private Integer liczbaProduktu;
    private Integer liczbaMagazynow;
    private Integer liczbaDostaw;
    
    private BigDecimal przychodyMiesiac;
    private BigDecimal wydatkiMiesiac;
    private BigDecimal zyskMiesiac;
    
    private Integer liczbaZamowienWProgress;
    private Integer liczbaZamowienDoRealizacji;
    
    private Integer liczbaProductowPonizejProgu;
}
