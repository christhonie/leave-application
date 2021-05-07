package za.co.dearx.leave.service.exception;

public class NotFoundException extends Exception {
    private static final long serialVersionUID = 267320492018013724L;

    /**
     * Indicate that an Entity was not found, with a reason.
     * This is the preferred constructor to use.
     * @param entity which was not found
     * @param reason why it was not found
     */
    public NotFoundException(String entity, String reason) {
        super(String.format("Entity %s was not found. %s", entity, reason));
    }

    public NotFoundException(String message) {
        super(message);
    }
}
