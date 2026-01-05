package state;

import model.Reservation;

/**
 * Stan: Zakończona (Completed)
 * Rezerwacja została zakończona pomyślnie (minął czas).
 *
 * Stan końcowy - brak możliwych akcji.
 */
public class CompletedState implements IReservationState {

    @Override
    public void confirm(Reservation reservation) {
        System.out.println("[STATE] Błąd: Nie można potwierdzić zakończonej rezerwacji #" + reservation.getId());
    }

    @Override
    public void cancel(Reservation reservation) {
        System.out.println("[STATE] Błąd: Nie można anulować zakończonej rezerwacji #" + reservation.getId());
    }

    @Override
    public void complete(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " jest już zakończona.");
    }

    @Override
    public String getStateName() {
        return "ZAKOŃCZONA";
    }
}
