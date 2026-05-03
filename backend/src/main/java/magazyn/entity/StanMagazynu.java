package magazyn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal ilosc;

    @OneToOne
    @JoinColumn(name = "id_produktu")
    @JsonIgnoreProperties("stanMagazynu")
    private Produkt produkt;
}