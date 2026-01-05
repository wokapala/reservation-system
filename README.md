# System Rezerwacji Sal Konferencyjnych

Projekt zaliczeniowy z Technologii Obiektowych - System zarządzania rezerwacjami sal w środowisku akademickim.

---

## Wymagania

- **Java:** JDK 17 lub nowszy
- **System operacyjny:** Windows, macOS, Linux

---

## Struktura projektu

```
reservation-system/
├── src/                    # Kod źródłowy
│   ├── model/              # Encje domenowe (User, Room, Reservation, etc.)
│   ├── state/              # State Pattern - stany rezerwacji
│   ├── observer/           # Observer Pattern - system powiadomień
│   ├── validator/          # Chain of Responsibility - walidacja
│   ├── builder/            # Builder Pattern - budowanie rezerwacji
│   ├── service/            # Warstwa logiki biznesowej
│   ├── controller/         # MVC - Controller
│   ├── view/               # MVC - View (interfejs konsolowy)
│   └── Main.java           # Punkt wejścia aplikacji
├── docs/                   # Dokumentacja
│   ├── UML-DIAGRAM-DESCRIPTION.md    # Opis diagramu UML
│   └── OPIS-PROJEKTU-SZABLON.md      # Szablon opisu projektu
├── bin/                    # Skompilowane pliki .class (generowane)
└── README.md               # Ten plik
```

---

## Kompilacja

### Windows:
```cmd
mkdir bin
javac -d bin -sourcepath src src\Main.java
```

### macOS/Linux:
```bash
mkdir -p bin
javac -d bin -sourcepath src src/Main.java
```

---

## Uruchomienie

### Tryb demonstracyjny (domyślny):
```bash
java -cp bin Main
```

Ten tryb automatycznie demonstruje wszystkie wzorce projektowe:
- Tworzenie rezerwacji (Builder)
- Walidację (Chain of Responsibility)
- Zarządzanie stanami (State)
- Powiadomienia (Observer)
- Wykrywanie konfliktów
- Cykl życia rezerwacji

### Tryb interaktywny:

1. Otwórz `src/Main.java`
2. Zakomentuj linię:
   ```java
   runAutomatedDemo(reservationService, users, rooms);
   ```
3. Odkomentuj linię:
   ```java
   runInteractiveMode(reservationService, users.get(0), rooms);
   ```
4. Przekompiluj i uruchom

W trybie interaktywnym możesz:
- Przeglądać sale
- Tworzyć rezerwacje
- Potwierdzać/anulować rezerwacje
- Wyświetlać swoje rezerwacje

---

## Zastosowane wzorce projektowe

### 1. State Pattern
**Lokalizacja:** `src/state/`
**Zastosowanie:** Zarządzanie cyklem życia rezerwacji (Pending → Confirmed → Completed/Cancelled)

### 2. Builder Pattern
**Lokalizacja:** `src/builder/ReservationBuilder.java`
**Zastosowanie:** Fluent interface do tworzenia obiektów Reservation

### 3. Chain of Responsibility
**Lokalizacja:** `src/validator/`
**Zastosowanie:** Wielopoziomowa walidacja rezerwacji (czas → uprawnienia → dostępność)

### 4. Observer Pattern
**Lokalizacja:** `src/observer/`
**Zastosowanie:** System powiadomień (email + logging) reagujący na zdarzenia

### 5. MVC Architecture
**Lokalizacja:** `src/model/`, `src/view/`, `src/controller/`
**Zastosowanie:** Separacja warstwy prezentacji, logiki i danych

---

## Realizacja zasad SOLID

### Single Responsibility Principle (SRP)
- Każda klasa ma jedną odpowiedzialność
- `AvailabilityChecker` - tylko sprawdzanie dostępności
- `ReservationService` - tylko orkiestracja (deleguje do innych)

### Open/Closed Principle (OCP)
- Nowy stan = nowa klasa `IReservationState`
- Nowy walidator = nowa klasa dziedzicząca `BaseValidator`
- Bez modyfikacji istniejącego kodu

### Liskov Substitution Principle (LSP)
- `Student` i `Lecturer` mogą zastąpić `User` bez zmian
- Wszystkie stany implementują `IReservationState`

### Interface Segregation Principle (ISP)
- Małe, wyspecjalizowane interfejsy (`IValidator`, `IReservationObserver`, `IReservationState`)

### Dependency Inversion Principle (DIP)
- Zależność od abstrakcji (interfejsów), nie implementacji
- Dependency Injection przez konstruktory

---

## Funkcjonalności

### System umożliwia:
- ✅ Przeglądanie dostępnych sal z wyposażeniem
- ✅ Tworzenie rezerwacji z walidacją:
  - Sprawdzanie przedziału czasowego (przyszłość, min. 1h)
  - Weryfikacja uprawnień (Student max 2h, Wykładowca unlimited)
  - Wykrywanie konfliktów czasowych
- ✅ Zarządzanie stanami rezerwacji (potwierdź/anuluj/zakończ)
- ✅ Automatyczne powiadomienia (email + logi)
- ✅ Przeglądanie rezerwacji użytkownika

---

## Przykład użycia (kod)

```java
// Użycie Builder Pattern
Reservation reservation = new ReservationBuilder()
    .forUser(student)
    .inRoom(room)
    .startingAt(LocalDateTime.now().plusDays(1))
    .withDuration(2)
    .withPurpose("Spotkanie koła naukowego")
    .build();

// Tworzenie rezerwacji (Chain of Responsibility waliduje)
reservationService.createReservation(reservation);

// Zmiana stanu (State Pattern)
reservationService.confirmReservation(reservation);

// Observer automatycznie wysyła powiadomienia
```

---

## Statystyki projektu

- **Łączna liczba klas:** 29
- **Interfejsy:** 3
- **Wzorce projektowe:** 5
- **Linie kodu:** ~1500+
- **Pakiety:** 8

### Podział klas po pakietach:
- `model`: 7 klas
- `state`: 5 klas (1 interfejs + 4 implementacje)
- `observer`: 5 klas (1 interfejs + implementacje)
- `validator`: 6 klas (1 interfejs + implementacje)
- `builder`: 1 klasa
- `service`: 2 klasy
- `controller`: 1 klasa
- `view`: 1 klasa
- `Main`: 1 klasa

---

## Demonstracja nieliniowości problemu

System rozwiązuje **złożony problem składający się z podproblemów**, które:
1. **Działają równocześnie** (nie sekwencyjnie)
2. **Wpływają na siebie nawzajem**
3. **Wymagają koordynacji** (wzorce projektowe)

**Przykład przepływu:**
```
Użytkownik tworzy rezerwację
    ↓
Builder buduje obiekt
    ↓
Chain of Responsibility waliduje (3 walidatory)
    ↓
AvailabilityChecker sprawdza konflikty
    ↓
State ustawia stan początkowy
    ↓
Observer wysyła powiadomienia (email + log)
    ↓
System rejestruje rezerwację
```

Zmiana w **dowolnym** elemencie wpływa na inne:
- Zmiana stanu → powiadomienia (Observer)
- Nowa rezerwacja → dostępność sal (AvailabilityChecker)
- Zmiana uprawnień → walidacja (Validator)

To jest **nieliniowość**.

---

## Dokumentacja

### UML Class Diagram
1. Otwórz `docs/UML-DIAGRAM-DESCRIPTION.md`
2. Skopiuj kod PlantUML do: https://www.plantuml.com/plantuml/uml/
3. Lub narysuj ręcznie w draw.io używając opisu

### Opis projektu
- Szablon: `docs/OPIS-PROJEKTU-SZABLON.md`
- Wypełnij dane osobowe i wyeksportuj do PDF

---

## Możliwe rozszerzenia (dla zainteresowanych)

System został zaprojektowany z myślą o rozszerzalności:

### Łatwe do dodania:
- ✅ Nowy typ użytkownika (np. Administrator) - dziedziczenie z `User`
- ✅ Nowy walidator (np. BudgetValidator) - implementacja `BaseValidator`
- ✅ Nowy typ powiadomień (np. SMS) - implementacja `IReservationObserver`
- ✅ Nowy stan rezerwacji - implementacja `IReservationState`
- ✅ Interfejs graficzny (GUI) - zamiana `ConsoleView` na `SwingView`
- ✅ Persystencja (baza danych) - dodanie `RepositoryPattern`

### Bez modyfikacji istniejącego kodu (Open/Closed Principle)

---

## Autor

**[TWOJE IMIĘ I NAZWISKO]**
Informatyka (zaocznie), rok III
Politechnika Krakowska

Projekt zaliczeniowy z Technologii Obiektowych
Data: [DATA]

---

## Licencja

Projekt edukacyjny - do użytku akademickiego.

---

## Podziękowania

Dziękuję prowadzącemu za kompleksowe wprowadzenie w tematykę wzorców projektowych i zasad SOLID.

---

**Koniec README**

**Powodzenia na obronie! 🚀**
