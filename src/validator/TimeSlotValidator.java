package validator;

import model.Reservation;
import java.time.LocalDateTime;

/**
 * Walidator sprawdzający czy przedział czasowy jest poprawny.
 * - Czas rozpoczęcia musi być w przyszłości
 * - Rezerwacja musi trwać przynajmniej 1 godzinę
 */
public class TimeSlotValidator extends BaseValidator {

    @Override
    protected void doValidate(Reservation reservation) throws ValidationException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = reservation.getTimeSlot().getStartTime();
        long duration = reservation.getTimeSlot().getDurationInHours();

        // Sprawdź czy rezerwacja jest w przyszłości
        if (start.isBefore(now)) {
            throw new ValidationException(
                "Nie można zarezerwować sali w przeszłości. Czas rozpoczęcia: " + start
            );
        }

        // Sprawdź minimalny czas trwania
        if (duration < 1) {
            throw new ValidationException(
                "Rezerwacja musi trwać przynajmniej 1 godzinę. Aktualna długość: " + duration + "h"
            );
        }

        System.out.println("[VALIDATOR] ✓ Przedział czasowy poprawny");
    }
}
