package magazyn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Encja reprezentująca sesję użytkownika w systemie.
 * Przechowuje token autoryzacyjny oraz informacje o czasie ważności sesji.
 */
@Entity
@Table(name = "sesje")
@Getter @Setter
public class Sesja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "uzytkownik_id", nullable = false)
    private Uzytkownik uzytkownik;

    @Column(nullable = false)
    private LocalDateTime dataUtworzenia;

    @Column(nullable = false)
    private LocalDateTime dataWygasniecia;

    /**
     * Konstruktor domyślny wymagany przez JPA/Hibernate.
     */
    public Sesja() {}

    /**
     * Konstruktor pomocniczy do tworzenia nowej sesji dla użytkownika.
     *
     * @param token unikalny token sesji
     * @param uzytkownik użytkownik, do którego przypisana jest sesja
     * @param minWaznosci czas ważności sesji w minutach
     */
    public Sesja(String token, Uzytkownik uzytkownik, int minWaznosci) {
        this.token = token;
        this.uzytkownik = uzytkownik;
        this.dataUtworzenia = LocalDateTime.now();
        this.dataWygasniecia = LocalDateTime.now().plusMinutes(minWaznosci);
    }
}
