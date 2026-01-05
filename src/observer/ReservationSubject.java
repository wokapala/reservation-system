package observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject (wydawca) w wzorcu Observer.
 * Zarządza listą obserwatorów i powiadamia ich o zdarzeniach.
 */
public class ReservationSubject {
    private List<IReservationObserver> observers = new ArrayList<>();

    /**
     * Dodaje obserwatora.
     */
    public void addObserver(IReservationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("[OBSERVER] Dodano obserwatora: " + observer.getClass().getSimpleName());
        }
    }

    /**
     * Usuwa obserwatora.
     */
    public void removeObserver(IReservationObserver observer) {
        observers.remove(observer);
        System.out.println("[OBSERVER] Usunięto obserwatora: " + observer.getClass().getSimpleName());
    }

    /**
     * Powiadamia wszystkich obserwatorów o zdarzeniu.
     */
    public void notifyObservers(ReservationEvent event) {
        for (IReservationObserver observer : observers) {
            observer.onReservationEvent(event);
        }
    }
}
