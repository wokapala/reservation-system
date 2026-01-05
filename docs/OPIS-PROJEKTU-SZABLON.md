# Projekt zaliczeniowy z Technologii Obiektowych
## System Rezerwacji Sal Konferencyjnych

---

**Autor:** [TWOJE IMIĘ I NAZWISKO]
**Nr albumu:** [NR ALBUMU]
**Kierunek:** Informatyka (zaoczne)
**Rok:** III
**Data:** [DATA]
**Prowadzący:** [IMIĘ I NAZWISKO PROWADZĄCEGO]

---

## 1. Opis problemu

System Rezerwacji Sal Konferencyjnych rozwiązuje złożony problem zarządzania rezerwacjami w środowisku akademickim, który charakteryzuje się:

### Podproblemy składowe (nieliniowy charakter):

1. **Zarządzanie dostępnością sal** - weryfikacja konfliktów czasowych, obsługa wyposażenia
2. **System uprawnień użytkowników** - różne limity czasowe dla studentów (2h) i wykładowców (bez limitu)
3. **Cykl życia rezerwacji** - złożony przepływ stanów (oczekująca → potwierdzona → zakończona/anulowana)
4. **System powiadomień** - reagowanie na zmiany stanów rezerwacji
5. **Walidacja wielopoziomowa** - sprawdzanie przedziału czasowego, uprawnień i dostępności

**Nieliniowość** przejawia się w tym, że:
- Zmiana stanu rezerwacji wpływa na system powiadomień
- Dostępność sali zależy od wszystkich aktywnych rezerwacji
- Uprawnienia użytkownika wpływają na możliwość utworzenia rezerwacji
- Wszystkie podproblemy działają równocześnie i współpracują ze sobą

---

## 2. Zastosowane wzorce projektowe

### 2.1 State Pattern
**Zastosowanie:** Zarządzanie cyklem życia rezerwacji

**Implementacja:**
- Interfejs `IReservationState` definiuje kontrakt
- Klasy: `PendingState`, `ConfirmedState`, `CancelledState`, `CompletedState`
- Każdy stan enkapsuluje zachowanie (confirm/cancel/complete)

**Uzasadnienie:** Eliminuje wielokrotne instrukcje warunkowe (if/switch), umożliwia łatwe dodawanie nowych stanów (OCP), enkapsuluje logikę przejść między stanami.

**Lokalizacja w kodzie:** `src/state/`

---

### 2.2 Builder Pattern
**Zastosowanie:** Tworzenie obiektów `Reservation`

**Implementacja:**
- Klasa `ReservationBuilder` z metodami fluent interface
- Metody: `forUser()`, `inRoom()`, `startingAt()`, `withDuration()`, `withPurpose()`, `build()`
- Walidacja przed utworzeniem obiektu

**Uzasadnienie:** Rezerwacja wymaga wielu parametrów, Builder zapewnia czytelny i bezpieczny sposób konstrukcji obiektu, separuje budowanie od reprezentacji.

**Lokalizacja w kodzie:** `src/builder/ReservationBuilder.java`

---

### 2.3 Chain of Responsibility
**Zastosowanie:** Walidacja rezerwacji

**Implementacja:**
- Interfejs `IValidator`, abstrakcyjna klasa `BaseValidator`
- Walidatory: `TimeSlotValidator` → `PermissionValidator` → `AvailabilityValidator`
- Każdy walidator sprawdza jeden aspekt i przekazuje dalej lub rzuca `ValidationException`

**Uzasadnienie:** Separacja odpowiedzialności (SRP), łatwe dodawanie nowych walidatorów, elastyczna kolejność walidacji.

**Lokalizacja w kodzie:** `src/validator/`

---

### 2.4 Observer Pattern
**Zastosowanie:** System powiadomień o zdarzeniach

**Implementacja:**
- `ReservationSubject` zarządza obserwatorami
- Interfejs `IReservationObserver`
- Konkretni obserwatorzy: `EmailNotificationService`, `LoggingService`
- Klasa `ReservationEvent` enkapsuluje informacje o zdarzeniu

**Uzasadnienie:** Luźne powiązanie między źródłem zdarzeń a odbiorcami, łatwe dodawanie nowych typów powiadomień, realizacja nieliniowości (zmiana stanu → automatyczne powiadomienia).

**Lokalizacja w kodzie:** `src/observer/`

---

### 2.5 MVC (Model-View-Controller)
**Zastosowanie:** Architektura całego systemu

**Implementacja:**
- **Model:** `src/model/` - encje domenowe (User, Room, Reservation, etc.)
- **View:** `src/view/ConsoleView.java` - interfejs użytkownika
- **Controller:** `src/controller/ReservationController.java` - logika aplikacji

**Uzasadnienie:** Separacja warstw prezentacji, logiki biznesowej i danych, łatwa wymiana interfejsu użytkownika, testowanie niezależne warstw.

---

## 3. Realizacja zasad SOLID

### Single Responsibility Principle (SRP)
- `AvailabilityChecker` - tylko sprawdzanie dostępności
- `ReservationService` - tylko orkiestracja (deleguje do innych)
- Każdy walidator - jeden aspekt walidacji
- `ConsoleView` - tylko prezentacja

### Open/Closed Principle (OCP)
- Nowy stan rezerwacji = nowa klasa implementująca `IReservationState`
- Nowy walidator = nowa klasa dziedzicząca `BaseValidator`
- Nowy typ powiadomień = nowa klasa implementująca `IReservationObserver`

### Liskov Substitution Principle (LSP)
- `Student` i `Lecturer` mogą zastąpić `User` bez zmian w zachowaniu
- Wszystkie stany mogą zastąpić `IReservationState`

### Interface Segregation Principle (ISP)
- `IReservationState` - tylko metody związane ze stanem
- `IReservationObserver` - tylko jedna metoda `onReservationEvent()`
- `IValidator` - tylko metody walidacji

### Dependency Inversion Principle (DIP)
- `ReservationService` zależy od `IValidator` (abstrakcja), nie konkretnych walidatorów
- `AvailabilityValidator` otrzymuje `AvailabilityChecker` przez konstruktor (Dependency Injection)
- Wszystkie komponenty wstrzykiwane przez konstruktor w `Main.java`

---

## 4. Struktura projektu

```
reservation-system/
├── src/
│   ├── model/           [6 klas]  - User, Student, Lecturer, Room, Equipment, TimeSlot, Reservation
│   ├── state/           [5 klas]  - IReservationState + 4 stany
│   ├── observer/        [5 klas]  - Event, Observer interface, Subject, 2 konkretne obserwatory
│   ├── validator/       [6 klas]  - IValidator, BaseValidator, 3 walidatory, Exception
│   ├── builder/         [1 klasa] - ReservationBuilder
│   ├── service/         [2 klasy] - ReservationService, AvailabilityChecker
│   ├── controller/      [1 klasa] - ReservationController
│   ├── view/            [1 klasa] - ConsoleView
│   └── Main.java
├── docs/
│   ├── UML-DIAGRAM-DESCRIPTION.md
│   └── OPIS-PROJEKTU.pdf (ten dokument)
└── README.md
```

**Łączna liczba klas:** 29 klas + 3 interfejsy = **32 elementy**

---

## 5. Funkcjonalności systemu

### Dla użytkownika:
- Przeglądanie dostępnych sal z wyposażeniem
- Tworzenie rezerwacji (z walidacją uprawnień i dostępności)
- Potwierdzanie rezerwacji
- Anulowanie rezerwacji
- Przeglądanie swoich rezerwacji

### Wewnętrzne (automatyczne):
- Walidacja wielopoziomowa (czas, uprawnienia, konflikty)
- Zarządzanie stanami rezerwacji
- Wysyłanie powiadomień (email, logi)
- Wykrywanie konfliktów czasowych

---

## 6. Demonstracja nieliniowości

Przykład przepływu przy tworzeniu rezerwacji:

1. **Builder** tworzy obiekt `Reservation`
2. **Chain of Responsibility** waliduje:
   - Przedział czasowy (przyszłość, min. 1h)
   - Uprawnienia użytkownika (limit czasowy)
   - Dostępność sali (brak konfliktów)
3. **State** ustawia stan początkowy (Pending)
4. **Observer** wysyła powiadomienia (email + log)
5. **AvailabilityChecker** rejestruje rezerwację
6. **Controller** aktualizuje widok

Wszystkie te podproblemy działają **równocześnie** i **wpływają na siebie**.

---

## 7. Technologie

- **Język:** Java 17
- **Architektura:** MVC
- **Wzorce:** State, Builder, Chain of Responsibility, Observer
- **Interfejs:** Konsola (Scanner)

---

## 8. Instrukcja uruchomienia

```bash
# Kompilacja
javac -d bin -sourcepath src src/Main.java

# Uruchomienie (tryb demonstracyjny)
java -cp bin Main

# Uruchomienie (tryb interaktywny)
# Odkomentuj linię w Main.java: runInteractiveMode(...)
```

---

## 9. Wnioski

Projekt demonstruje:
- ✅ Dekompozycję złożonego problemu na podproblemy współpracujące (nieliniowość)
- ✅ Zastosowanie 5 wzorców projektowych w uzasadniony sposób
- ✅ Pełną zgodność z zasadami SOLID
- ✅ Architekturę MVC z wyraźną separacją warstw
- ✅ Kod gotowy do rozbudowy (OCP) i testowania

System jest skalowalny - łatwo dodać:
- Nowe typy użytkowników (Factory Pattern)
- Nowe walidatory (Chain of Responsibility)
- Nowe typy powiadomień (Observer)
- Nowe stany rezerwacji (State)

---

**Koniec dokumentu**

---

## Uwagi do wypełnienia:

1. Uzupełnij dane osobowe na początku
2. Dostosuj numer strony jeśli drukujesz (max 2 strony)
3. Możesz dodać screenshoty w załączniku (nie wliczają się do limitu 2 stron)
4. Diagram UML dołącz jako osobny plik (PNG/PDF)
