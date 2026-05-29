package magazyn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "konfiguracja_systemu")
public class Konfiguracja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nazwa_parametru", nullable = false, unique = true, length = 100)
    private String nazwaParametru;

    @Column(name = "wartosc_parametru", columnDefinition = "TEXT")
    private String wartoscParametru;

    @Column(name = "typ_parametru", length = 50) // STRING, INTEGER, BOOLEAN, DECIMAL
    private String typParametru;

    @Column(name = "opis", columnDefinition = "TEXT")
    private String opis;

    @Column(name = "aktywna", nullable = false)
    private Boolean aktywna = true;
}
