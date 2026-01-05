package service;

import model.Reservation;
import validator.*;
import observer.*;
import builder.ReservationBuilder;

/**
 * Główny serwis zarządzający rezerwacjami.
 * Orkiestruje współpracę między różnymi komponentami systemu.
 *
 * Single Responsibility: koordynacja procesu rezerwacji (nie implementacja logiki biznesowej).
 * Dependency Injection: przyjmuje zależności przez konstruktor.
 */
public class ReservationService {
    private AvailabilityChecker availabilityChecker;
    private IValidator validatorChain;
    private ReservationSubject eventPublisher;

    public ReservationService(
        AvailabilityChecker availabilityChecker,
        ReservationSubject eventPublisher
    ) {
        this.availabilityChecker = availabilityChecker;
        this.eventPublisher = eventPublisher;
        this.validatorChain = buildValidatorChain();
    }

    /**
     * Buduje łańcuch walidatorów (Chain of Responsibility).
     */
    private IValidator buildValidatorChain() {
        // Tworzymy łańcuch: TimeSlot -> Permission -> Availability
        IValidator timeSlotValidator = new TimeSlotValidator();
        IValidator permissionValidator = new PermissionValidator();
        IValidator availabilityValidator = new AvailabilityValidator(availabilityChecker);

        timeSlotValidator.setNext(permissionValidator);
        permissionValidator.setNext(availabilityValidator);

        return timeSlotValidator; // Zwracamy pierwszy w łańcuchu
    }

    /**
     * Tworzy nową rezerwację (z walidacją).
     * @throws ValidationException jeśli walidacja nie powiedzie się
     */
    public Reservation createReservation(Reservation reservation) throws ValidationException {
        System.out.println("\n=== Tworzenie nowej rezerwacji ===");

        // Walidacja przez łańcuch walidatorów
        validatorChain.validate(reservation);

        // Rejestracja w systemie
        availabilityChecker.registerReservation(reservation);

        // Powiadomienie obserwatorów
        ReservationEvent event = new ReservationEvent(reservation, ReservationEvent.EventType.CREATED);
        eventPublisher.notifyObservers(event);

        System.out.println("✓ Rezerwacja utworzona pomyślnie: " + reservation.getId());
        return reservation;
    }

    /**
     * Potwierdza rezerwację (zmiana stanu).
     */
    public void confirmReservation(Reservation reservation) {
        System.out.println("\n=== Potwierdzanie rezerwacji #" + reservation.getId() + " ===");
        reservation.confirm();

        // Powiadomienie obserwatorów
        ReservationEvent event = new ReservationEvent(reservation, ReservationEvent.EventType.CONFIRMED);
        eventPublisher.notifyObservers(event);
    }

    /**
     * Anuluje rezerwację (zmiana stanu).
     */
    public void cancelReservation(Reservation reservation) {
        System.out.println("\n=== Anulowanie rezerwacji #" + reservation.getId() + " ===");
        reservation.cancel();

        // Powiadomienie obserwatorów
        ReservationEvent event = new ReservationEvent(reservation, ReservationEvent.EventType.CANCELLED);
        eventPublisher.notifyObservers(event);
    }

    /**
     * Kończy rezerwację (zmiana stanu).
     */
    public void completeReservation(Reservation reservation) {
        System.out.println("\n=== Kończenie rezerwacji #" + reservation.getId() + " ===");
        reservation.complete();

        // Powiadomienie obserwatorów
        ReservationEvent event = new ReservationEvent(reservation, ReservationEvent.EventType.COMPLETED);
        eventPublisher.notifyObservers(event);
    }

    /**
     * Getter dla AvailabilityChecker (potrzebny w kontrolerze).
     */
    public AvailabilityChecker getAvailabilityChecker() {
        return availabilityChecker;
    }
}
