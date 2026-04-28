package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class HistoriaZamowieniaDTO {
    private Integer id;
    private OffsetDateTime data;
    private String nazwaDostawcy;
    private Integer status;
    private Long sumaProduktow;
    private Float kwota;
}