package view;

import model.*;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Warstwa View w architekturze MVC.
 * Odpowiada za interakcję z użytkownikiem przez konsolę.
 *
 * Single Responsibility: tylko prezentacja i zbieranie danych wejściowych.
 */
public class ConsoleView {
    private Scanner scanner;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Wyświetla menu główne.
     */
    public void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   SYSTEM REZERWACJI SAL KONFERENCYJNYCH");
        System.out.println("=".repeat(50));
        System.out.println("1. Pokaż dostępne sale");
        System.out.println("2. Utwórz nową rezerwację");
        System.out.println("3. Pokaż moje rezerwacje");
        System.out.println("4. Potwierdź rezerwację");
        System.out.println("5. Anuluj rezerwację");
        System.out.println("6. Zakończ rezerwację");
        System.out.println("0. Wyjście");
        System.out.println("=".repeat(50));
        System.out.print("Wybierz opcję: ");
    }

    /**
     * Odczytuje wybór użytkownika z menu.
     */
    public int getUserChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            return choice;
        } catch (Exception e) {
            scanner.nextLine(); // clear buffer
            return -1;
        }
    }

    /**
     * Wyświetla listę sal.
     */
    public void displayRooms(List<Room> rooms) {
        System.out.println("\n--- Dostępne Sale ---");
        for (int i = 0; i < rooms.size(); i++) {
            System.out.println((i + 1) + ". " + rooms.get(i));
        }
    }

    /**
     * Wyświetla listę rezerwacji.
     */
    public void displayReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("\nBrak rezerwacji.");
            return;
        }

        System.out.println("\n--- Rezerwacje ---");
        for (Reservation r : reservations) {
            System.out.println(r);
            System.out.println("-".repeat(40));
        }
    }

    /**
     * Pobiera wybór sali od użytkownika.
     */
    public int selectRoom(int maxIndex) {
        System.out.print("Wybierz salę (1-" + maxIndex + "): ");
        int choice = getUserChoice();
        return choice - 1; // Convert to 0-based index
    }

    /**
     * Pobiera czas rozpoczęcia od użytkownika.
     */
    public LocalDateTime getStartTime() {
        System.out.print("Czas rozpoczęcia (format: yyyy-MM-dd HH:mm): ");
        String input = scanner.nextLine();
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (Exception e) {
            System.out.println("Błędny format daty. Użyto domyślnej wartości (teraz + 1h).");
            return LocalDateTime.now().plusHours(1);
        }
    }

    /**
     * Pobiera długość rezerwacji od użytkownika.
     */
    public int getDuration() {
        System.out.print("Długość rezerwacji (w godzinach): ");
        return getUserChoice();
    }

    /**
     * Pobiera cel rezerwacji od użytkownika.
     */
    public String getPurpose() {
        System.out.print("Cel rezerwacji: ");
        return scanner.nextLine();
    }

    /**
     * Pobiera ID rezerwacji od użytkownika.
     */
    public String getReservationId() {
        System.out.print("Podaj ID rezerwacji: ");
        return scanner.nextLine();
    }

    /**
     * Wyświetla komunikat o sukcesie.
     */
    public void displaySuccess(String message) {
        System.out.println("\n✓ " + message);
    }

    /**
     * Wyświetla komunikat o błędzie.
     */
    public void displayError(String message) {
        System.out.println("\n✗ BŁĄD: " + message);
    }

    /**
     * Wyświetla ogólny komunikat.
     */
    public void displayMessage(String message) {
        System.out.println("\n" + message);
    }

    /**
     * Zatrzymuje wykonanie do naciśnięcia Enter.
     */
    public void waitForEnter() {
        System.out.print("\nNaciśnij Enter aby kontynuować...");
        scanner.nextLine();
    }

    /**
     * Zamyka scanner.
     */
    public void close() {
        scanner.close();
    }
}
