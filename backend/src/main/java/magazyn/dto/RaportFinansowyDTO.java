package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RaportFinansowyDTO {

    private LocalDateTime dataPoczatek;
    private LocalDateTime dataKoniec;
    private BigDecimal sumaPrzychodow;
    private BigDecimal sumaWydatkow;
    private BigDecimal sumaZysku;
    private Integer liczbaTransakcji;
    private String typ;

    public RaportFinansowyDTO(LocalDateTime dataPoczatek, LocalDateTime dataKoniec, 
                              BigDecimal sumaPrzychodow, BigDecimal sumaWydatkow, BigDecimal sumaZysku) {
        this.dataPoczatek = dataPoczatek;
        this.dataKoniec = dataKoniec;
        this.sumaPrzychodow = sumaPrzychodow != null ? sumaPrzychodow : BigDecimal.ZERO;
        this.sumaWydatkow = sumaWydatkow != null ? sumaWydatkow : BigDecimal.ZERO;
        this.sumaZysku = sumaZysku != null ? sumaZysku : BigDecimal.ZERO;
    }
}
