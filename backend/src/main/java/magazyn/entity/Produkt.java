package magazyn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "produkty")
public class Produkt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nazwa_produktu")
    private String nazwaProduktu;

    @Column(name = "opis_produktu")
    private String opisProduktu;

    @Column(name = "kod_kreskowy")
    private String kodKreskowy;

    private BigDecimal cena;

    @Column(name = "id_dostawcy")
    private Integer idDostawcy;

    @OneToOne(mappedBy = "produkt", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("produkt")
    private StanMagazynu stanMagazynu;

    @Column(length = 20, nullable = false)
    private String jednostka;

    @Column(columnDefinition = "text")
    private String strefa = "strefa_A";
}