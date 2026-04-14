package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "zamowienia_zaopatrzeniowiec")
public class ZamowienieZaopatrzeniowca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private OffsetDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dostawcy", nullable = false)
    private Dostawca dostawca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uzytkownika", nullable = false)
    private Uzytkownik uzytkownik;

    @Column(nullable = false)
    private Integer status;
}