package state;

import model.Reservation;

/**
 * Interfejs dla State pattern.
 * Definiuje kontrakt dla wszystkich stanów rezerwacji.
 *
 * Realizuje:
 * - Open/Closed Principle: nowy stan = nowa klasa implementująca interfejs
 * - Polimorfizm: różne zachowania w zależności od stanu
 */
public interface IReservationState {
    /**
     * Próba potwierdzenia rezerwacji.
     */
    void confirm(Reservation reservation);

    /**
     * Próba anulowania rezerwacji.
     */
    void cancel(Reservation reservation);

    /**
     * Próba zakończenia rezerwacji.
     */
    void complete(Reservation reservation);

    /**
     * Nazwa stanu do wyświetlania.
     */
    String getStateName();
}
