package model;

/**
 * Student - może rezerwować sale maksymalnie na 2 godziny.
 */
public class Student extends User {
    private static final int MAX_BOOKING_HOURS = 2;

    public Student(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public int getMaxBookingHours() {
        return MAX_BOOKING_HOURS;
    }

    @Override
    public String getUserType() {
        return "Student";
    }
}
