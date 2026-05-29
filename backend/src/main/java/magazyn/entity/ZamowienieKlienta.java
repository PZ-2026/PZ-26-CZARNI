package magazyn.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Encja reprezentująca zamówienie złożone przez klienta.
 * Zawiera informacje o dacie, kliencie, przypisanym magazynierze oraz statusie zamówienia.
 */
@Getter
@Setter
@Entity
@Table(name = "zamowienia_klienci")
public class ZamowienieKlienta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private OffsetDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_klienta", nullable = false)
    private Uzytkownik klient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_magazyniera")
    private Uzytkownik magazynier;

    @OneToMany(mappedBy = "zamowienie", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ZamowienieProduktyKlienci> pozycje;

    @Column(nullable = false)
    private Integer status;
}
