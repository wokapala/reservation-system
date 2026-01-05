package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Sala konferencyjna z wyposażeniem.
 */
public class Room {
    private String id;
    private String name;
    private int capacity;
    private List<Equipment> equipment;

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.equipment = new ArrayList<>();
    }

    public void addEquipment(Equipment eq) {
        this.equipment.add(eq);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Equipment> getEquipment() {
        return new ArrayList<>(equipment); // defensive copy
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(capacity).append(" osób)");
        if (!equipment.isEmpty()) {
            sb.append(" - Wyposażenie: ");
            for (int i = 0; i < equipment.size(); i++) {
                sb.append(equipment.get(i));
                if (i < equipment.size() - 1) sb.append(", ");
            }
        }
        return sb.toString();
    }
}
