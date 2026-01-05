package validator;

import model.Reservation;

/**
 * Walidator sprawdzający uprawnienia użytkownika.
 * - Student może rezerwować max 2h
 * - Wykładowca bez limitu
 */
public class PermissionValidator extends BaseValidator {

    @Override
    protected void doValidate(Reservation reservation) throws ValidationException {
        int maxHours = reservation.getUser().getMaxBookingHours();
        long requestedHours = reservation.getTimeSlot().getDurationInHours();

        if (requestedHours > maxHours) {
            throw new ValidationException(
                reservation.getUser().getUserType() + " może rezerwować maksymalnie " +
                maxHours + "h. Żądano: " + requestedHours + "h"
            );
        }

        System.out.println("[VALIDATOR] ✓ Uprawnienia użytkownika poprawne");
    }
}
