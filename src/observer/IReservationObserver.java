package observer;

/**
 * Interfejs obserwatora dla wzorca Observer.
 * Definiuje kontrakt dla wszystkich klas, które chcą reagować na zdarzenia rezerwacji.
 */
public interface IReservationObserver {
    /**
     * Metoda wywoływana gdy wystąpi zdarzenie w systemie rezerwacji.
     */
    void onReservationEvent(ReservationEvent event);
}
