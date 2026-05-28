package magazyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa startowa aplikacji Systemu Zarządzania Magazynem.
 * Inicjalizuje kontekst Spring Boot i uruchamia serwer aplikacji.
 */
@SpringBootApplication(scanBasePackages = "magazyn")
public class Main {
    /**
     * Metoda główna (entry point) aplikacji.
     *
     * @param args parametry wiersza poleceń przekazywane do aplikacji
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
