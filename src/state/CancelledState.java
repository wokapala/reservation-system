package state;

import model.Reservation;

/**
 * Stan: Anulowana (Cancelled)
 * Rezerwacja została anulowana i jest nieaktywna.
 *
 * Stan końcowy - brak możliwych akcji.
 */
public class CancelledState implements IReservationState {

    @Override
    public void confirm(Reservation reservation) {
        System.out.println("[STATE] Błąd: Nie można potwierdzić anulowanej rezerwacji #" + reservation.getId());
    }

    @Override
    public void cancel(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " jest już anulowana.");
    }

    @Override
    public void complete(Reservation reservation) {
        System.out.println("[STATE] Błąd: Nie można zakończyć anulowanej rezerwacji #" + reservation.getId());
    }

    @Override
    public String getStateName() {
        return "ANULOWANA";
    }
}
