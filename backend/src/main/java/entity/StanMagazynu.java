package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "stan_magazynu")
public class StanMagazynu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produktu", nullable = false)
    private Produkt produkt;

    @Column(length = 20, nullable = false)
    private String jednostka;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal ilosc;

    @Column(columnDefinition = "text")
    private String strefa = "strefa_A";
}