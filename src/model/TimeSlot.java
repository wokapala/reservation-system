package model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Przedział czasowy rezerwacji.
 */
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Czas rozpoczęcia musi być wcześniejszy niż zakończenia");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Oblicza długość rezerwacji w godzinach.
     */
    public long getDurationInHours() {
        return Duration.between(startTime, endTime).toHours();
    }

    /**
     * Sprawdza czy przedziały czasowe nachodzą na siebie.
     */
    public boolean overlapsWith(TimeSlot other) {
        return !this.endTime.isBefore(other.startTime) &&
               !this.startTime.isAfter(other.endTime);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return startTime.format(formatter) + " - " + endTime.format(formatter);
    }
}
