package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaZamowieniaDTO {
    private Integer id;
    private OffsetDateTime data;
    private String nazwaDostawcy;
    private Integer status;
    private Long sumaProduktow;
    private Float kwota;

    public HistoriaZamowieniaDTO(Integer id, OffsetDateTime data, String nazwaDostawcy, Integer status, Long sumaProduktow) {
        this.id = id;
        this.data = data;
        this.nazwaDostawcy = nazwaDostawcy;
        this.status = status;
        this.sumaProduktow = sumaProduktow;
        this.kwota = 0.0f; // Zapytanie nie zwraca kwoty, więc przypisujemy domyślne 0
    }
}