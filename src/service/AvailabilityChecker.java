package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Serwis sprawdzający dostępność sal.
 * Single Responsibility: tylko weryfikacja dostępności.
 */
public class AvailabilityChecker {
    private List<Reservation> allReservations;

    public AvailabilityChecker() {
        this.allReservations = new ArrayList<>();
    }

    /**
     * Rejestruje rezerwację w systemie (aby sprawdzać konflikty).
     */
    public void registerReservation(Reservation reservation) {
        allReservations.add(reservation);
    }

    /**
     * Sprawdza czy sala jest dostępna w danym przedziale czasowym.
     * Weryfikuje konflikty z istniejącymi rezerwacjami.
     */
    public boolean isRoomAvailable(Room room, TimeSlot timeSlot) {
        for (Reservation existing : allReservations) {
            // Sprawdzamy tylko potwierdzone lub oczekujące rezerwacje
            String stateName = existing.getState().getStateName();
            boolean isActive = stateName.equals("POTWIERDZONA") || stateName.equals("OCZEKUJĄCA");

            if (isActive &&
                existing.getRoom().getId().equals(room.getId()) &&
                existing.getTimeSlot().overlapsWith(timeSlot)) {
                return false; // Znaleziono konflikt
            }
        }
        return true; // Brak konfliktów
    }

    /**
     * Zwraca listę wszystkich rezerwacji dla danej sali.
     */
    public List<Reservation> getReservationsForRoom(Room room) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : allReservations) {
            if (r.getRoom().getId().equals(room.getId())) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Zwraca listę wszystkich rezerwacji dla danego użytkownika.
     */
    public List<Reservation> getReservationsForUser(User user) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : allReservations) {
            if (r.getUser().getId().equals(user.getId())) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Zwraca wszystkie rezerwacje.
     */
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(allReservations);
    }
}
