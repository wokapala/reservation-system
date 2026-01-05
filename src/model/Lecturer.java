package model;

/**
 * Wykładowca - może rezerwować sale bez ograniczenia czasowego.
 */
public class Lecturer extends User {
    private static final int MAX_BOOKING_HOURS = Integer.MAX_VALUE; // unlimited

    public Lecturer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public int getMaxBookingHours() {
        return MAX_BOOKING_HOURS;
    }

    @Override
    public String getUserType() {
        return "Wykładowca";
    }
}
