package observer;

import model.Reservation;

/**
 * Zdarzenie w systemie rezerwacji.
 * Enkapsuluje informacje o zdarzeniu dla obserwatorów.
 */
public class ReservationEvent {
    public enum EventType {
        CREATED,
        CONFIRMED,
        CANCELLED,
        COMPLETED
    }

    private Reservation reservation;
    private EventType eventType;
    private long timestamp;

    public ReservationEvent(Reservation reservation, EventType eventType) {
        this.reservation = reservation;
        this.eventType = eventType;
        this.timestamp = System.currentTimeMillis();
    }

    public Reservation getReservation() {
        return reservation;
    }

    public EventType getEventType() {
        return eventType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "[EVENT: " + eventType + "] Rezerwacja #" + reservation.getId() +
               " - " + reservation.getUser().getName();
    }
}
