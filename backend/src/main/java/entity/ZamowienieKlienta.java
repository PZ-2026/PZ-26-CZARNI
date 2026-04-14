package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

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

    @Column(nullable = false)
    private Integer status;
}