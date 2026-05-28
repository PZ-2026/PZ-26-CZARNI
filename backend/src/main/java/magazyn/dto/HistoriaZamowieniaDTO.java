package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

/**
 * Obiekt DTO reprezentujący skrócone informacje o zamówieniu na liście historii.
 * Wykorzystywany zarówno dla klientów, jak i zaopatrzeniowców.
 */
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

    /**
     * Konstruktor wykorzystywany przez zapytania JPQL, które nie obliczają kwoty zamówienia.
     *
     * @param id identyfikator zamówienia
     * @param data data złożenia zamówienia
     * @param nazwaDostawcy nazwa dostawcy lub podmiotu realizującego
     * @param status aktualny status zamówienia
     * @param sumaProduktow łączna liczba sztuk produktów w zamówieniu
     */
    public HistoriaZamowieniaDTO(Integer id, OffsetDateTime data, String nazwaDostawcy, Integer status, Long sumaProduktow) {
        this.id = id;
        this.data = data;
        this.nazwaDostawcy = nazwaDostawcy;
        this.status = status;
        this.sumaProduktow = sumaProduktow;
        this.kwota = 0.0f; // Domyślna wartość, gdy kwota nie jest przesyłana
    }
}
