package magazyn.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UzytkownikAdminDTO {

    private Integer id;

    @NotBlank(message = "Imię jest wymagane")
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    private String nazwisko;

    @NotBlank(message = "Numer telefonu jest wymagany")
    @Pattern(regexp = "^[0-9]{9}$", message = "Numer telefonu musi mieć 9 cyfr")
    private String telefon;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Niepoprawny format adresu email")
    private String email;

    @NotNull(message = "Rola jest wymagana")
    @Min(value = 0, message = "Rola musi być liczbą od 0 do 4")
    @Max(value = 4, message = "Rola musi być liczbą od 0 do 4")
    private Integer rola;

    private String nazwaRoli; // Opis roli do wyświetlania (administrator, magazynier, zaopatrzeniowiec, klient)

    private String firma;

    @Pattern(regexp = "^[0-9]{10}$", message = "NIP musi składać się z dokładnie 10 cyfr")
    private String nip;

    private Boolean zablokowany = false;
}
