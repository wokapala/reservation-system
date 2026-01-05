package state;

import model.Reservation;

/**
 * Stan: Oczekująca (Pending)
 * Rezerwacja utworzona, ale nie potwierdzona przez system.
 *
 * Możliwe akcje:
 * - confirm() -> ConfirmedState
 * - cancel() -> CancelledState
 * - complete() -> błąd (nie można zakończyć niepotwierdzonej rezerwacji)
 */
public class PendingState implements IReservationState {

    @Override
    public void confirm(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " potwierdzona.");
        reservation.setState(new ConfirmedState());
    }

    @Override
    public void cancel(Reservation reservation) {
        System.out.println("[STATE] Rezerwacja #" + reservation.getId() + " anulowana (przed potwierdzeniem).");
        reservation.setState(new CancelledState());
    }

    @Override
    public void complete(Reservation reservation) {
        System.out.println("[STATE] Błąd: Nie można zakończyć niepotwierdzonej rezerwacji #" + reservation.getId());
    }

    @Override
    public String getStateName() {
        return "OCZEKUJĄCA";
    }
}
