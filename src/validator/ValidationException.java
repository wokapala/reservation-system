package validator;

/**
 * Wyjątek rzucany gdy walidacja rezerwacji nie powiedzie się.
 */
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
