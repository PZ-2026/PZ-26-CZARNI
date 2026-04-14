package entity;

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

    @Column(name = "nazwa_produktu", nullable = false, columnDefinition = "text")
    private String nazwaProduktu;

    @Column(name = "opis_produktu", columnDefinition = "text")
    private String opisProduktu;

    @Column(name = "kod_kreskowy", nullable = false, unique = true, columnDefinition = "text")
    private String kodKreskowy;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cena;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dostawcy", nullable = false)
    private Dostawca dostawca;
}