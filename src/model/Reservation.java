package model;

import state.IReservationState;
import state.PendingState;

/**
 * Rezerwacja sali - centralna encja biznesowa.
 * Wykorzystuje State pattern do zarządzania cyklem życia rezerwacji.
 */
public class Reservation {
    private String id;
    private User user;
    private Room room;
    private TimeSlot timeSlot;
    private String purpose;
    private IReservationState state;

    public Reservation(String id, User user, Room room, TimeSlot timeSlot, String purpose) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.timeSlot = timeSlot;
        this.purpose = purpose;
        this.state = new PendingState(); // domyślny stan: oczekująca
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public String getPurpose() {
        return purpose;
    }

    public IReservationState getState() {
        return state;
    }

    public void setState(IReservationState state) {
        this.state = state;
    }

    /**
     * Delegacja do State pattern - potwierdź rezerwację.
     */
    public void confirm() {
        state.confirm(this);
    }

    /**
     * Delegacja do State pattern - anuluj rezerwację.
     */
    public void cancel() {
        state.cancel(this);
    }

    /**
     * Delegacja do State pattern - zakończ rezerwację.
     */
    public void complete() {
        state.complete(this);
    }

    @Override
    public String toString() {
        return "Rezerwacja #" + id + " [" + state.getStateName() + "]\n" +
               "  Użytkownik: " + user.getName() + "\n" +
               "  Sala: " + room.getName() + "\n" +
               "  Czas: " + timeSlot + "\n" +
               "  Cel: " + purpose;
    }
}
