package validator;

import model.Reservation;

/**
 * Interfejs dla wzorca Chain of Responsibility.
 * Definiuje kontrakt dla walidatorów rezerwacji.
 */
public interface IValidator {
    /**
     * Ustawia następny walidator w łańcuchu.
     */
    void setNext(IValidator validator);

    /**
     * Waliduje rezerwację. Jeśli walidacja przechodzi, przekazuje do następnego w łańcuchu.
     * @throws ValidationException jeśli walidacja nie powiedzie się
     */
    void validate(Reservation reservation) throws ValidationException;
}
