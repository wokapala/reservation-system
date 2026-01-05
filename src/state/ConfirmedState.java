package state;

import model.Reservation;

/**
 * Stan: Potwierdzona (Confirmed)
 * Rezerwacja zatwierdzona przez system i aktywna.
 *
 * Możliwe akcje:
 * - confirm() -> już potwierdzona
 * - cancel() -> CancelledState
 * - complete() -> CompletedState
 */
public class ConfirmedState implements IReservationState {

    @Override
    public void confirm(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " jest już potwierdzona.");
    }

    @Override
    public void cancel(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " anulowana.");
        reservation.setState(new CancelledState());
    }

    @Override
    public void complete(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " zakończona pomyślnie.");
        reservation.setState(new CompletedState());
    }

    @Override
    public String getStateName() {
        return "POTWIERDZONA";
    }
}
