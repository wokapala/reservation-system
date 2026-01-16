import model.*;
import service.*;
import view.ConsoleView;
import controller.ReservationController;
import observer.*;
import builder.ReservationBuilder;
import validator.ValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Punkt wejścia aplikacji.
 * Inicjalizuje wszystkie komponenty systemu i uruchamia aplikację.
 *
 * Demonstracja wzorców projektowych:
 * - MVC (separacja warstw)
 * - Dependency Injection (wstrzykiwanie zależności)
 * - Observer (system powiadomień)
 * - State (zarządzanie stanami rezerwacji)
 * - Builder (tworzenie rezerwacji)
 * - Chain of Responsibility (walidacja)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("  SYSTEM REZERWACJI SAL KONFERENCYJNYCH");
        System.out.println("  Projekt z Technologii Obiektowych");
        System.out.println("=".repeat(60));

        // 1. Inicjalizacja danych testowych
        List<Room> rooms = initializeRooms();
        List<User> users = initializeUsers();

        // 2. Inicjalizacja warstwy serwisowej
        AvailabilityChecker availabilityChecker = new AvailabilityChecker();

        // 3. Inicjalizacja Observer pattern
        ReservationSubject eventPublisher = new ReservationSubject();
        eventPublisher.addObserver(new EmailNotificationService());
        eventPublisher.addObserver(new LoggingService());

        // 4. Inicjalizacja głównego serwisu
        ReservationService reservationService = new ReservationService(
            availabilityChecker,
            eventPublisher
        );

        // 5. Demonstracja automatyczna (można zakomentować i przejść do interaktywnego trybu)
        //System.out.println("\n>>> DEMONSTRACJA AUTOMATYCZNA <<<\n");
        //runAutomatedDemo(reservationService, users, rooms);

        // 6. Tryb interaktywny (odkomentuj aby używać)
        runInteractiveMode(reservationService, users.get(0), rooms);
    }

    /**
     * Inicjalizuje dane testowe - sale.
     */
    private static List<Room> initializeRooms() {
        List<Room> rooms = new ArrayList<>();

        Room small = new Room("R1", "Sala Mała", 10);
        small.addEquipment(new Equipment("Projektor", 1));
        small.addEquipment(new Equipment("Tablica", 1));

        Room medium = new Room("R2", "Sala Średnia", 30);
        medium.addEquipment(new Equipment("Projektor", 1));
        medium.addEquipment(new Equipment("Tablica interaktywna", 1));
        medium.addEquipment(new Equipment("System audio", 1));

        Room large = new Room("R3", "Sala Duża (Aula)", 100);
        large.addEquipment(new Equipment("Projektor", 2));
        large.addEquipment(new Equipment("Mikrofony", 4));
        large.addEquipment(new Equipment("System nagłośnieniowy", 1));

        rooms.add(small);
        rooms.add(medium);
        rooms.add(large);

        return rooms;
    }

    /**
     * Inicjalizuje dane testowe - użytkowników.
     */
    private static List<User> initializeUsers() {
        List<User> users = new ArrayList<>();

        users.add(new Student("U1", "Jan Kowalski", "jan.kowalski@student.pk.edu.pl"));
        users.add(new Lecturer("U2", "Dr Anna Nowak", "anna.nowak@pk.edu.pl"));

        return users;
    }

    /**
     * Demonstracja automatyczna - pokazuje wszystkie wzorce w akcji.
     */
    @SuppressWarnings("unused")
    private static void runAutomatedDemo(
        ReservationService service,
        List<User> users,
        List<Room> rooms
    ) {
        User student = users.get(0);
        User lecturer = users.get(1);
        Room smallRoom = rooms.get(0);
        Room mediumRoom = rooms.get(1);

        try {
            // === TEST 1: Poprawna rezerwacja studenta (2h) ===
            System.out.println("\n### TEST 1: Student rezerwuje salę na 2h ###");
            Reservation r1 = new ReservationBuilder()
                .forUser(student)
                .inRoom(smallRoom)
                .startingAt(LocalDateTime.now().plusDays(1))
                .withDuration(2)
                .withPurpose("Spotkanie koła naukowego")
                .build();
            service.createReservation(r1);

            // === TEST 2: Potwierdzenie rezerwacji (State pattern) ===
            System.out.println("\n### TEST 2: Potwierdzanie rezerwacji ###");
            service.confirmReservation(r1);

            // === TEST 3: Próba rezerwacji w tym samym czasie (konflikt) ===
            System.out.println("\n### TEST 3: Próba rezerwacji w konflikcie czasowym ###");
            try {
                Reservation r2 = new ReservationBuilder()
                    .forUser(lecturer)
                    .inRoom(smallRoom)
                    .startingAt(LocalDateTime.now().plusDays(1).plusMinutes(30))
                    .withDuration(2)
                    .withPurpose("Konsultacje")
                    .build();
                service.createReservation(r2);
            } catch (ValidationException e) {
                System.out.println("✓ Poprawnie wykryto konflikt: " + e.getMessage());
            }

            // === TEST 4: Student próbuje zarezerwować na 3h (przekroczenie limitu) ===
            System.out.println("\n### TEST 4: Student próbuje przekroczyć limit 2h ###");
            try {
                Reservation r3 = new ReservationBuilder()
                    .forUser(student)
                    .inRoom(mediumRoom)
                    .startingAt(LocalDateTime.now().plusDays(2))
                    .withDuration(3)
                    .withPurpose("Warsztat")
                    .build();
                service.createReservation(r3);
            } catch (ValidationException e) {
                System.out.println("✓ Poprawnie wykryto przekroczenie uprawnień: " + e.getMessage());
            }

            // === TEST 5: Wykładowca rezerwuje na 4h (bez problemu) ===
            System.out.println("\n### TEST 5: Wykładowca rezerwuje na 4h ###");
            Reservation r4 = new ReservationBuilder()
                .forUser(lecturer)
                .inRoom(mediumRoom)
                .startingAt(LocalDateTime.now().plusDays(2))
                .withDuration(4)
                .withPurpose("Wykład")
                .build();
            service.createReservation(r4);
            service.confirmReservation(r4);

            // === TEST 6: Cykl życia rezerwacji (State pattern) ===
            System.out.println("\n### TEST 6: Pełny cykl życia rezerwacji ###");
            Reservation r5 = new ReservationBuilder()
                .forUser(lecturer)
                .inRoom(rooms.get(2))
                .startingAt(LocalDateTime.now().plusDays(3))
                .withDuration(2)
                .withPurpose("Konferencja")
                .build();
            service.createReservation(r5);
            System.out.println("Stan początkowy: " + r5.getState().getStateName());

            service.confirmReservation(r5);
            System.out.println("Po potwierdzeniu: " + r5.getState().getStateName());

            service.completeReservation(r5);
            System.out.println("Po zakończeniu: " + r5.getState().getStateName());

            // Próba potwierdzenia zakończonej rezerwacji (błąd)
            System.out.println("\nPróba potwierdzenia zakończonej rezerwacji:");
            service.confirmReservation(r5);

            // === TEST 7: Anulowanie rezerwacji ===
            System.out.println("\n### TEST 7: Anulowanie rezerwacji ###");
            service.cancelReservation(r1);

            System.out.println("\n" + "=".repeat(60));
            System.out.println("  DEMONSTRACJA ZAKOŃCZONA POMYŚLNIE");
            System.out.println("  Wszystkie wzorce projektowe zostały zademonstrowane:");
            System.out.println("  ✓ State Pattern (zarządzanie stanami)");
            System.out.println("  ✓ Builder Pattern (tworzenie rezerwacji)");
            System.out.println("  ✓ Chain of Responsibility (walidacja)");
            System.out.println("  ✓ Observer Pattern (powiadomienia)");
            System.out.println("  ✓ MVC Architecture");
            System.out.println("=".repeat(60));

        } catch (ValidationException e) {
            System.err.println("Nieoczekiwany błąd walidacji: " + e.getMessage());
        }
    }

    /**
     * Tryb interaktywny - uruchamia aplikację z interfejsem konsolowym.
     */
    private static void runInteractiveMode(
        ReservationService service,
        User currentUser,
        List<Room> rooms
    ) {
        ConsoleView view = new ConsoleView();
        ReservationController controller = new ReservationController(
            view,
            service,
            currentUser,
            rooms
        );

        controller.run();
        view.close();
    }
}
