# Uruchomienie backendu w Dockerze od zera

## 1. Uruchom Docker Desktop

Otwórz Docker Desktop i poczekaj, aż będzie gotowy do pracy.

Jeśli Docker nie działa, komendy `docker compose` pokażą błąd podobny do:

```text
dockerDesktopLinuxEngine: The system cannot find the file specified
```

## 2. Otwórz terminal w katalogu backendu

W PowerShell przejdź do katalogu backendu:

```powershell
cd C:\Users\wojci\PZ-26-CZARNI\backend
```

## 3. Zbuduj i uruchom backend

```powershell
docker compose up --build
```

Pierwsze uruchomienie może potrwać dłużej, bo Docker pobiera obraz Javy i Gradle pobiera zależności.

Po poprawnym starcie backend działa na:

```text
http://localhost:8080/
```

## 4. Uruchom frontend w emulatorze Androida

W Android Studio uruchom aplikację frontendową na emulatorze.

Frontend jest ustawiony na adres:

```text
http://10.0.2.2:8080/
```

To jest poprawny adres backendu dla emulatora Androida.

## 5. Jeśli uruchamiasz aplikację na fizycznym telefonie

Adres `10.0.2.2` działa tylko w emulatorze. Na telefonie trzeba użyć adresu IP komputera w tej samej sieci Wi-Fi.

### Krok 1: podłącz telefon i komputer do tej samej sieci

Telefon i komputer muszą być w tej samej sieci lokalnej, najlepiej na tym samym Wi-Fi.

### Krok 2: sprawdź IP komputera

W PowerShell wpisz:

```powershell
ipconfig
```

Znajdź kartę Wi-Fi i pole:

```text
IPv4 Address
```

Przykład:

```text
192.168.1.50
```

### Krok 3: zmień adres backendu we frontendzie

Otwórz plik:

```text
frontend/app/build.gradle.kts
```

Zmień:

```kotlin
buildConfigField("String", "BACKEND_URL", "\"http://10.0.2.2:8080/\"")
```

na adres IP komputera, np.:

```kotlin
buildConfigField("String", "BACKEND_URL", "\"http://192.168.1.50:8080/\"")
```

### Krok 4: uruchom backend w Dockerze

W katalogu backendu:

```powershell
cd C:\Users\wojci\PZ-26-CZARNI\backend
docker compose up --build
```

### Krok 5: przebuduj i uruchom aplikację na telefonie

Po zmianie `BACKEND_URL` przebuduj aplikację w Android Studio i uruchom ją na telefonie.

### Jeśli telefon dalej nie łączy się z backendem

Sprawdź:

- czy telefon i komputer są w tej samej sieci Wi-Fi,
- czy backend działa w Dockerze,
- czy adres IP w `BACKEND_URL` jest aktualny,
- czy port `8080` nie jest blokowany przez zaporę Windows,
- czy w przeglądarce telefonu działa adres `http://IP_KOMPUTERA:8080/`, np. `http://192.168.1.50:8080/`.

## 6. Zatrzymanie backendu

W terminalu, w którym działa `docker compose up`, naciśnij:

```text
Ctrl + C
```

Potem możesz usunąć uruchomiony kontener:

```powershell
docker compose down
```

## Przydatne komendy

Sprawdzenie działających kontenerów:

```powershell
docker ps
```

Podgląd logów backendu:

```powershell
docker compose logs -f backend
```

Uruchomienie bez ponownego budowania:

```powershell
docker compose up
```

Przebudowanie od zera:

```powershell
docker compose build --no-cache
docker compose up
```
