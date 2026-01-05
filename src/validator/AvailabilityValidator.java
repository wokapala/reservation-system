package validator;

import model.Reservation;
import service.AvailabilityChecker;

/**
 * Walidator sprawdzający dostępność sali.
 * Współpracuje z AvailabilityChecker (wstrzykiwany przez konstruktor - Dependency Injection).
 */
public class AvailabilityValidator extends BaseValidator {
    private AvailabilityChecker availabilityChecker;

    public AvailabilityValidator(AvailabilityChecker availabilityChecker) {
        this.availabilityChecker = availabilityChecker;
    }

    @Override
    protected void doValidate(Reservation reservation) throws ValidationException {
        boolean isAvailable = availabilityChecker.isRoomAvailable(
            reservation.getRoom(),
            reservation.getTimeSlot()
        );

        if (!isAvailable) {
            throw new ValidationException(
                "Sala '" + reservation.getRoom().getName() +
                "' jest już zarezerwowana w tym czasie: " + reservation.getTimeSlot()
            );
        }

        System.out.println("[VALIDATOR] ✓ Sala dostępna w wybranym terminie");
    }
}
