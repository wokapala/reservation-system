package builder;

import model.*;
import java.time.LocalDateTime;

/**
 * Builder pattern dla tworzenia obiektów Reservation.
 * Umożliwia krok po kroku budowanie złożonego obiektu rezerwacji.
 *
 * Realizuje:
 * - Separację konstrukcji od reprezentacji
 * - Fluent interface (chainable methods)
 * - Walidację przed utworzeniem obiektu
 */
public class ReservationBuilder {
    private static int nextId = 1;

    private String id;
    private User user;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;

    public ReservationBuilder() {
        this.id = String.valueOf(nextId++);
    }

    /**
     * Ustawia użytkownika rezerwacji.
     */
    public ReservationBuilder forUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Ustawia salę.
     */
    public ReservationBuilder inRoom(Room room) {
        this.room = room;
        return this;
    }

    /**
     * Ustawia czas rozpoczęcia.
     */
    public ReservationBuilder startingAt(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    /**
     * Ustawia czas zakończenia.
     */
    public ReservationBuilder endingAt(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    /**
     * Ustawia czas trwania (alternatywa dla endingAt).
     */
    public ReservationBuilder withDuration(int hours) {
        if (this.startTime == null) {
            throw new IllegalStateException("Najpierw ustaw czas rozpoczęcia (startingAt)");
        }
        this.endTime = this.startTime.plusHours(hours);
        return this;
    }

    /**
     * Ustawia cel rezerwacji.
     */
    public ReservationBuilder withPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Buduje i zwraca obiekt Reservation.
     * Waliduje czy wszystkie wymagane pola zostały ustawione.
     */
    public Reservation build() {
        validateRequiredFields();

        TimeSlot timeSlot = new TimeSlot(startTime, endTime);
        return new Reservation(id, user, room, timeSlot, purpose);
    }

    /**
     * Waliduje czy wszystkie wymagane pola zostały ustawione.
     */
    private void validateRequiredFields() {
        if (user == null) {
            throw new IllegalStateException("Użytkownik nie został ustawiony");
        }
        if (room == null) {
            throw new IllegalStateException("Sala nie została ustawiona");
        }
        if (startTime == null) {
            throw new IllegalStateException("Czas rozpoczęcia nie został ustawiony");
        }
        if (endTime == null) {
            throw new IllegalStateException("Czas zakończenia nie został ustawiony");
        }
        if (purpose == null || purpose.trim().isEmpty()) {
            throw new IllegalStateException("Cel rezerwacji nie został ustawiony");
        }
    }

    /**
     * Resetuje builder do ponownego użycia.
     */
    public ReservationBuilder reset() {
        this.id = String.valueOf(nextId++);
        this.user = null;
        this.room = null;
        this.startTime = null;
        this.endTime = null;
        this.purpose = null;
        return this;
    }
}
