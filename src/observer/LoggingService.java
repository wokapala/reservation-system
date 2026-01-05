package observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Konkretny obserwator - serwis logowania zdarzeń.
 * Zapisuje wszystkie zdarzenia w systemie (w rzeczywistości do pliku/bazy).
 */
public class LoggingService implements IReservationObserver {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onReservationEvent(ReservationEvent event) {
        String logEntry = formatLogEntry(event);

        // Symulacja zapisu do logu (w rzeczywistości zapis do pliku/bazy)
        System.out.println("[LOG] " + logEntry);
    }

    private String formatLogEntry(ReservationEvent event) {
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp + " | " + event.getEventType() + " | " +
               "Rezerwacja #" + event.getReservation().getId() + " | " +
               "Użytkownik: " + event.getReservation().getUser().getName() + " | " +
               "Sala: " + event.getReservation().getRoom().getName();
    }
}
