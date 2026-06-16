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

Frontend domyślnie łączy się z:

```text
http://10.0.2.2:8080/
```

To jest poprawny adres backendu dla emulatora Androida.

## 5. Jeśli uruchamiasz aplikację na fizycznym telefonie

Adres `10.0.2.2` działa tylko w emulatorze. Na telefonie backend musi być dostępny pod adresem IP komputera w tej samej sieci Wi-Fi.

Sprawdź IP komputera:

```powershell
ipconfig
```

Szukaj aktywnej karty sieciowej z bramą domyślną, np.:

```text
IPv4 Address: 192.168.68.106
Default Gateway: 192.168.68.1
```

Telefon powinien być w tej samej sieci, np. też `192.168.68.x`.

Jeśli aplikacja ma działać na telefonie, adres backendu w buildzie frontendu musi wskazywać IP komputera, np.:

```text
http://192.168.68.106:8080/
```

Jeśli telefon nie otwiera tego adresu w przeglądarce, sprawdź:

- czy backend działa w Dockerze,
- czy telefon i komputer są w tej samej sieci Wi-Fi,
- czy używasz właściwego IP komputera, a nie karty wirtualnej typu `192.168.56.1`,
- czy Zapora Windows nie blokuje portu `8080`.

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
