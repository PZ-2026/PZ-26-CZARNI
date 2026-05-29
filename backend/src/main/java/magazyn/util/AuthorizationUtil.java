package magazyn.util;

import magazyn.entity.Uzytkownik;
import org.springframework.stereotype.Component;

/**
 * Utility class for handling authorization and role management
 */
@Component
public class AuthorizationUtil {

    // Role constants
    public static final int ROLE_ADMINISTRATOR = 1;
    public static final int ROLE_MAGAZYNIER = 2;
    public static final int ROLE_ZAOPATRZENIOWIEC = 3;
    public static final int ROLE_KLIENT = 4;

    /**
     * Sprawdź czy użytkownik jest administratorem
     */
    public static boolean isAdmin(Uzytkownik user) {
        return user != null && user.getRola() != null && user.getRola() == ROLE_ADMINISTRATOR;
    }

    /**
     * Sprawdź czy użytkownik jest magazynnierem
     */
    public static boolean isMagazynier(Uzytkownik user) {
        return user != null && user.getRola() != null && user.getRola() == ROLE_MAGAZYNIER;
    }

    /**
     * Sprawdź czy użytkownik jest zaopatrzeniowcem
     */
    public static boolean isZaopatrzeniowiec(Uzytkownik user) {
        return user != null && user.getRola() != null && user.getRola() == ROLE_ZAOPATRZENIOWIEC;
    }

    /**
     * Sprawdź czy użytkownik jest klientem
     */
    public static boolean isKlient(Uzytkownik user) {
        return user != null && user.getRola() != null && user.getRola() == ROLE_KLIENT;
    }

    /**
     * Sprawdź czy użytkownik jest zablokowany
     */
    public static boolean isBlocked(Uzytkownik user) {
        return user != null && user.getZablokowany();
    }

    /**
     * Pobierz nazwę roli
     */
    public static String getRoleName(int role) {
        switch (role) {
            case ROLE_ADMINISTRATOR:
                return "Administrator";
            case ROLE_MAGAZYNIER:
                return "Magazynier";
            case ROLE_ZAOPATRZENIOWIEC:
                return "Zaopatrzeniowiec";
            case ROLE_KLIENT:
                return "Klient";
            default:
                return "Nieznana rola";
        }
    }

    /**
     * Pobierz ID roli na podstawie nazwy
     */
    public static int getRoleId(String roleName) {
        switch (roleName.toLowerCase()) {
            case "administrator":
                return ROLE_ADMINISTRATOR;
            case "magazynier":
                return ROLE_MAGAZYNIER;
            case "zaopatrzeniowiec":
                return ROLE_ZAOPATRZENIOWIEC;
            case "klient":
                return ROLE_KLIENT;
            default:
                throw new IllegalArgumentException("Nieznana rola: " + roleName);
        }
    }

    /**
     * Sprawdzenie czy mamy dostęp do operacji admin
     */
    public static void validateAdminAccess(Uzytkownik user) throws IllegalAccessException {
        if (user == null || !isAdmin(user)) {
            throw new IllegalAccessException("Brak dostępu. Operacja wymaga uprawnień administratora");
        }
        if (isBlocked(user)) {
            throw new IllegalAccessException("Konto zostało zablokowane");
        }
    }
}
