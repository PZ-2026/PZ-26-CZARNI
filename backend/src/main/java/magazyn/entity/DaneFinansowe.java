package magazyn.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "dane_finansowe")
public class DaneFinansowe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal przychody;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal wydatki;

    @Column(precision = 12, scale = 2)
    private BigDecimal zysk;

    @Column(length = 255)
    private String typ; // SPRZEDAZ, ZAKUP, etc.

    @Column(name = "id_zamowienia")
    private Integer idZamowienia;

    @PrePersist
    protected void onCreate() {
        data = LocalDateTime.now();
        if (przychody != null && wydatki != null) {
            zysk = przychody.subtract(wydatki);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (przychody != null && wydatki != null) {
            zysk = przychody.subtract(wydatki);
        }
    }
}
