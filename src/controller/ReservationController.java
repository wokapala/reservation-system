package controller;

import model.*;
import service.*;
import view.ConsoleView;
import builder.ReservationBuilder;
import validator.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Kontroler w architekturze MVC.
 * Pośredniczy między View a warstwą biznesową (Service).
 *
 * Single Responsibility: obsługa logiki aplikacji (nie biznesowej).
 * Dependency Injection: wszystkie zależności przez konstruktor.
 */
public class ReservationController {
    private ConsoleView view;
    private ReservationService reservationService;
    private User currentUser;
    private List<Room> availableRooms;

    public ReservationController(
        ConsoleView view,
        ReservationService reservationService,
        User currentUser,
        List<Room> availableRooms
    ) {
        this.view = view;
        this.reservationService = reservationService;
        this.currentUser = currentUser;
        this.availableRooms = availableRooms;
    }

    /**
     * Główna pętla aplikacji.
     */
    public void run() {
        boolean running = true;

        view.displayMessage("Zalogowano jako: " + currentUser);

        while (running) {
            view.displayMainMenu();
            int choice = view.getUserChoice();

            switch (choice) {
                case 1:
                    handleShowRooms();
                    break;
                case 2:
                    handleCreateReservation();
                    break;
                case 3:
                    handleShowMyReservations();
                    break;
                case 4:
                    handleConfirmReservation();
                    break;
                case 5:
                    handleCancelReservation();
                    break;
                case 6:
                    handleCompleteReservation();
                    break;
                case 0:
                    running = false;
                    view.displayMessage("Do widzenia!");
                    break;
                default:
                    view.displayError("Nieprawidłowa opcja");
            }

            if (running && choice != 0) {
                view.waitForEnter();
            }
        }
    }

    /**
     * Obsługuje wyświetlanie listy sal.
     */
    private void handleShowRooms() {
        view.displayRooms(availableRooms);
    }

    /**
     * Obsługuje tworzenie nowej rezerwacji (wykorzystuje Builder).
     */
    private void handleCreateReservation() {
        view.displayRooms(availableRooms);

        int roomIndex = view.selectRoom(availableRooms.size());
        if (roomIndex < 0 || roomIndex >= availableRooms.size()) {
            view.displayError("Nieprawidłowy wybór sali");
            return;
        }

        Room selectedRoom = availableRooms.get(roomIndex);
        LocalDateTime startTime = view.getStartTime();
        int duration = view.getDuration();
        String purpose = view.getPurpose();

        try {
            // Użycie Builder pattern
            Reservation reservation = new ReservationBuilder()
                .forUser(currentUser)
                .inRoom(selectedRoom)
                .startingAt(startTime)
                .withDuration(duration)
                .withPurpose(purpose)
                .build();

            // Delegacja do serwisu
            reservationService.createReservation(reservation);

            view.displaySuccess("Rezerwacja utworzona: #" + reservation.getId());
        } catch (ValidationException e) {
            view.displayError(e.getMessage());
        } catch (IllegalStateException e) {
            view.displayError("Błąd budowania rezerwacji: " + e.getMessage());
        }
    }

    /**
     * Obsługuje wyświetlanie rezerwacji użytkownika.
     */
    private void handleShowMyReservations() {
        List<Reservation> userReservations = reservationService
            .getAvailabilityChecker()
            .getReservationsForUser(currentUser);

        view.displayReservations(userReservations);
    }

    /**
     * Obsługuje potwierdzanie rezerwacji.
     */
    private void handleConfirmReservation() {
        Reservation reservation = findReservationById();
        if (reservation != null) {
            reservationService.confirmReservation(reservation);
            view.displaySuccess("Rezerwacja potwierdzona");
        }
    }

    /**
     * Obsługuje anulowanie rezerwacji.
     */
    private void handleCancelReservation() {
        Reservation reservation = findReservationById();
        if (reservation != null) {
            reservationService.cancelReservation(reservation);
            view.displaySuccess("Rezerwacja anulowana");
        }
    }

    /**
     * Obsługuje kończenie rezerwacji.
     */
    private void handleCompleteReservation() {
        Reservation reservation = findReservationById();
        if (reservation != null) {
            reservationService.completeReservation(reservation);
            view.displaySuccess("Rezerwacja zakończona");
        }
    }

    /**
     * Pomocnicza metoda do znajdowania rezerwacji po ID.
     */
    private Reservation findReservationById() {
        String id = view.getReservationId();
        List<Reservation> userReservations = reservationService
            .getAvailabilityChecker()
            .getReservationsForUser(currentUser);

        for (Reservation r : userReservations) {
            if (r.getId().equals(id)) {
                return r;
            }
        }

        view.displayError("Nie znaleziono rezerwacji o ID: " + id);
        return null;
    }
}
