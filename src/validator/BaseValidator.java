package validator;

import model.Reservation;

/**
 * Abstrakcyjna klasa bazowa dla walidatorów.
 * Implementuje logikę łańcucha odpowiedzialności (Chain of Responsibility).
 */
public abstract class BaseValidator implements IValidator {
    protected IValidator next;

    @Override
    public void setNext(IValidator validator) {
        this.next = validator;
    }

    /**
     * Template method - wykonuje walidację i przekazuje do następnego w łańcuchu.
     */
    @Override
    public void validate(Reservation reservation) throws ValidationException {
        // Wykonaj własną walidację
        doValidate(reservation);

        // Jeśli jest następny walidator, przekaż do niego
        if (next != null) {
            next.validate(reservation);
        }
    }

    /**
     * Konkretna logika walidacji - implementowana przez podklasy.
     */
    protected abstract void doValidate(Reservation reservation) throws ValidationException;
}
