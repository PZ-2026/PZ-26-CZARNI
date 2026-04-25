package magazyn.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "uzytkownicy")
public class Uzytkownik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Imię jest wymagane")
    @Column(nullable = false)
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Column(nullable = false)
    private String nazwisko;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @jakarta.validation.constraints.Pattern(regexp = "^[0-9]{9}$", message = "Numer telefonu musi mieć 9 cyfr")
    @Column(length = 9, nullable = false)
    private String telefon;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Niepoprawny format adresu email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 8, message = "Hasło musi mieć minimum 8 znaków")
    @Column(nullable = false)
    private String haslo;

    @NotNull(message = "Rola jest wymagana")
    @Column(nullable = false)
    private Integer rola;

    @Column(length = 100)
    private String firma;

    @jakarta.validation.constraints.Pattern(regexp = "^[0-9]{10}$", message = "NIP musi składać się z dokładnie 10 cyfr")
    @Column(length = 10)
    private String nip;
}