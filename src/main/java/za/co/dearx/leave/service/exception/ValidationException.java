package za.co.dearx.leave.service.exception;

public class ValidationException extends Exception {
    private static final long serialVersionUID = 2699244334265617009L;

    /**
     * An Entity or Message contains invalid data.
     * @param entity or message which was found invalid
     * @param reason why it is invalid
     */
    public ValidationException(String entity, String reason) {
        super(String.format("Entity or message %s failed validation. %s", entity, reason));
    }

    public ValidationException(String message) {
        super(message);
    }
}
