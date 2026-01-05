package observer;

/**
 * Konkretny obserwator - serwis powiadomień email.
 * W rzeczywistej implementacji wysyłałby emaile, tutaj symuluje wysyłkę.
 */
public class EmailNotificationService implements IReservationObserver {

    @Override
    public void onReservationEvent(ReservationEvent event) {
        String email = event.getReservation().getUser().getEmail();
        String message = generateEmailMessage(event);

        // Symulacja wysyłki emaila
        System.out.println("\n[EMAIL] Wysłano wiadomość do: " + email);
        System.out.println("  Temat: " + getEmailSubject(event));
        System.out.println("  Treść: " + message);
    }

    private String getEmailSubject(ReservationEvent event) {
        switch (event.getEventType()) {
            case CREATED:
                return "Utworzono rezerwację #" + event.getReservation().getId();
            case CONFIRMED:
                return "Potwierdzono rezerwację #" + event.getReservation().getId();
            case CANCELLED:
                return "Anulowano rezerwację #" + event.getReservation().getId();
            case COMPLETED:
                return "Zakończono rezerwację #" + event.getReservation().getId();
            default:
                return "Powiadomienie o rezerwacji";
        }
    }

    private String generateEmailMessage(ReservationEvent event) {
        return "Witaj " + event.getReservation().getUser().getName() + ",\n" +
               "  Status rezerwacji sali '" + event.getReservation().getRoom().getName() +
               "' zmienił się na: " + event.getReservation().getState().getStateName();
    }
}
