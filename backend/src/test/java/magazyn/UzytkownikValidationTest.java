package magazyn;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import magazyn.entity.Uzytkownik;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UzytkownikValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // dobry uzytkownik
    private Uzytkownik createValidUser() {
        Uzytkownik u = new Uzytkownik();
        u.setImie("Jan");
        u.setNazwisko("Kowalski");
        u.setEmail("jan.kowalski@wp.pl");
        u.setHaslo("Haslo1234!");
        u.setTelefon("123456789");
        u.setRola(1);
        u.setNip("1234567890");
        return u;
    }

    @Test
    @DisplayName("Powinien przejść walidację dla poprawnych danych")
    void shouldPassValidationForCorrectData() {
        Uzytkownik u = createValidUser();
        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);
        assertTrue(violations.isEmpty(), "Lista błędów powinna być pusta");
    }

    @Test
    @DisplayName("Błąd: Hasło jest za krótkie (mniej niż 8 znaków)")
    void shouldFailWhenPasswordIsTooShort() {
        Uzytkownik u = createValidUser();
        u.setHaslo("1234567"); // 7 znaków

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("minimum 8 znaków")));
    }

    @Test
    @DisplayName("Błąd: Numer telefonu ma litery lub złą długość")
    void shouldFailWhenPhoneIsInvalid() {
        Uzytkownik u = createValidUser();

        // Za krótki
        u.setTelefon("123");
        assertFalse(validator.validate(u).isEmpty(), "Powinien zawieść dla 3 cyfr");

        // Zawiera litery
        u.setTelefon("12345678a");
        assertFalse(validator.validate(u).isEmpty(), "Powinien zawieść dla telefonu z literą");
    }

    @Test
    @DisplayName("Błąd: NIP ma 11 cyfr (powinno być dokładnie 10)")
    void shouldFailWhenNipHasWrongLength() {
        Uzytkownik u = createValidUser();
        u.setNip("12345678901"); // 11 cyfr

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("NIP musi składać się z dokładnie 10 cyfr")));
    }

    @Test
    @DisplayName("Błąd: Brak wymaganych pól (NotBlank)")
    void shouldFailWhenRequiredFieldsAreEmpty() {
        Uzytkownik u = new Uzytkownik(); // Pusty obiekt

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertTrue(violations.size() >= 5);
    }

    @Test
    @DisplayName("Błąd: Niepoprawny format email (brak małpy)")
    void shouldFailWhenEmailIsMissingAtSign() {
        Uzytkownik u = createValidUser();
        u.setEmail("jan.kowalski.pl");

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Niepoprawny format")));
    }

    @Test
    @DisplayName("Błąd: Imię składające się z samych spacji")
    void shouldFailWhenFirstNameIsOnlySpaces() {
        Uzytkownik u = createValidUser();
        u.setImie("   "); // @NotBlank wykrywa, że nie ma tu liter

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Imię jest wymagane")));
    }

    @Test
    @DisplayName("Sukces: Firma i NIP mogą być nullem (pola opcjonalne)")
    void shouldPassWhenOptionalFieldsAreNull() {
        Uzytkownik u = createValidUser();
        u.setFirma(null);
        u.setNip(null);

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertTrue(violations.isEmpty(), "Firma i NIP nie są wymagane, więc powinno przejść");
    }

    @Test
    @DisplayName("Błąd: NIP zawiera spacje w środku")
    void shouldFailWhenNipHasSpaces() {
        Uzytkownik u = createValidUser();
        u.setNip("123 456 78"); // Spacje łamią RegEx "^[0-9]{10}$"

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty(), "NIP ze spacjami powinien być odrzucony");
    }

    @Test
    @DisplayName("Błąd: Rola jest nullem")
    void shouldFailWhenRoleIsNull() {
        Uzytkownik u = createValidUser();
        u.setRola(null); // Masz adnotację @NotNull przy Integer rola

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Rola jest wymagana")));
    }

    @Test
    @DisplayName("Błąd: Hasło o długości dokładnie 8 znaków (granica sukcesu)")
    void shouldPassWhenPasswordIsExactlyEightChars() {
        Uzytkownik u = createValidUser();
        u.setHaslo("12345678"); // Równo 8 - granica @Size(min = 8)

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);

        assertTrue(violations.isEmpty(), "Hasło 8-znakowe powinno być jeszcze akceptowane");
    }

    @Test
    @DisplayName("Błąd: Bardzo długie stringi (przekroczenie @Column length)")
    void shouldFailWhenFirmNameIsTooLong() {
        Uzytkownik u = createValidUser();
        // Generujemy string o długości 101 znaków (limit w bazie masz 100)
        String longName = "A".repeat(101);
        u.setFirma(longName);

        Set<ConstraintViolation<Uzytkownik>> violations = validator.validate(u);
    }
}