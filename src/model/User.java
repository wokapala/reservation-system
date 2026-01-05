package model;

/**
 * Reprezentuje użytkownika systemu rezerwacji.
 * Wykorzystuje dziedziczenie do rozróżnienia typów użytkowników (Student, Wykładowca).
 */
public abstract class User {
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Maksymalny czas rezerwacji w godzinach (zależy od typu użytkownika).
     * Realizuje Open/Closed Principle - zachowanie zdefiniowane w podklasach.
     */
    public abstract int getMaxBookingHours();

    /**
     * Typ użytkownika dla wyświetlania.
     */
    public abstract String getUserType();

    @Override
    public String toString() {
        return getUserType() + ": " + name + " (" + email + ")";
    }
}
