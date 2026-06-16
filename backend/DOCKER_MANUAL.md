# Uruchomienie backendu w Dockerze

## Wymagania

Przed uruchomieniem upewnij się, że masz:

- zainstalowany Docker Desktop,
- uruchomiony Docker Engine,
- sklonowane repozytorium projektu,
- dostęp do internetu przy pierwszym buildzie.

## 1. Przejdź do katalogu backendu

Otwórz terminal w katalogu projektu i przejdź do folderu backendu:

```bash
cd backend
```

Jeśli jesteś w innym miejscu na dysku, najpierw przejdź do katalogu repozytorium, a dopiero potem do `backend`.

## 2. Zbuduj i uruchom backend

W katalogu `backend` uruchom:

```bash
docker compose up --build
```

Podczas pierwszego uruchomienia Docker może pobierać obrazy bazowe, a Gradle zależności projektu. To może potrwać kilka minut.

Po poprawnym starcie backend jest dostępny na komputerze pod adresem:

```text
http://localhost:8080/
```

Backend uruchamia tez lokalna baze PostgreSQL w kontenerze `backend-db`.
Przy pierwszym starcie baza jest tworzona z pliku `magazyn.sql`.

Domyslne dane bazy w Dockerze:

```text
host z komputera: localhost
port z komputera: 5433
baza: magazyn_db
uzytkownik: postgres
haslo: postgres
```

Wewnatrz sieci Dockera backend laczy sie z baza przez:

```text
jdbc:postgresql://db:5432/magazyn_db
```

Jesli zmienisz plik `magazyn.sql` i chcesz zaladowac dump od nowa, usun wolumen bazy:

```bash
docker compose down -v
docker compose up --build
```

## 3. Uruchom frontend na emulatorze Androida

Jeśli aplikacja działa na emulatorze Androida, frontend powinien łączyć się z backendem przez:

```text
http://10.0.2.2:8080/
```

`10.0.2.2` to specjalny adres, który z emulatora wskazuje na komputer hosta.

## 4. Uruchom frontend na fizycznym telefonie

Jeśli aplikacja działa na fizycznym telefonie, telefon nie może używać `10.0.2.2`.

Telefon musi łączyć się z adresem IP komputera w tej samej sieci lokalnej, np.:

```text
http://192.168.1.50:8080/
```

Jak znaleźć IP komputera:

- Windows: `ipconfig`
- Linux/macOS: `ip addr` albo `ifconfig`

Wybierz adres aktywnej karty sieciowej, która ma bramę domyślną. Nie używaj adresów kart wirtualnych, VPN ani sieci host-only.

Telefon i komputer muszą być w tej samej sieci Wi-Fi/LAN.

Jeśli telefon nie widzi backendu, sprawdź:

- czy backend działa w Dockerze,
- czy port `8080` jest wystawiony,
- czy telefon i komputer są w tej samej sieci,
- czy zapora systemowa nie blokuje portu `8080`,
- czy w przeglądarce telefonu działa `http://IP_KOMPUTERA:8080/`.

## 5. Zmiana adresu backendu we frontendzie

Adres backendu jest ustawiony w pliku:

```text
frontend/app/build.gradle.kts
```

Szukaj linii:

```kotlin
buildConfigField("String", "BACKEND_URL", "\"http://10.0.2.2:8080/\"")
```

Dla emulatora Androida zostaw:

```kotlin
buildConfigField("String", "BACKEND_URL", "\"http://10.0.2.2:8080/\"")
```

Dla fizycznego telefonu wpisz IP komputera w sieci lokalnej, np.:

```kotlin
buildConfigField("String", "BACKEND_URL", "\"http://192.168.1.50:8080/\"")
```

Po zmianie adresu przebuduj aplikację Android, żeby nowa wartość trafiła do `BuildConfig`.

## 6. Zatrzymanie backendu

W terminalu, w którym działa backend, naciśnij:

```text
Ctrl + C
```

Następnie możesz usunąć kontener:

```bash
docker compose down
```

## Przydatne komendy

Sprawdzenie działających kontenerów:

```bash
docker ps
```

Podgląd logów backendu:

```bash
docker compose logs -f backend
```

Uruchomienie bez ponownego budowania:

```bash
docker compose up
```

Przebudowanie obrazu od zera:

```bash
docker compose build --no-cache
docker compose up
```

Sprawdzenie konfiguracji compose:

```bash
docker compose config
```

## Typowe problemy

### Docker nie jest uruchomiony

Jeśli pojawia się błąd połączenia z Docker Engine, uruchom Docker Desktop i poczekaj, aż będzie gotowy.

### Port 8080 jest zajęty

Jeśli port `8080` jest zajęty, zatrzymaj proces używający tego portu albo zmień mapowanie portu w `docker-compose.yml`.

### Telefon nie łączy się z backendem

Najczęstsze przyczyny:

- wpisany został adres emulatora `10.0.2.2`,
- wpisany został adres karty wirtualnej,
- telefon jest w innej sieci niż komputer,
- firewall blokuje port `8080`.
