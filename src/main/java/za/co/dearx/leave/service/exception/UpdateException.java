package za.co.dearx.leave.service.exception;

public class UpdateException extends Exception {

    private static final long serialVersionUID = -1999466386432544037L;

    private final String reason;

    /**
     * Indicate that an Entity could not be updated, with a reason.
     * This is the preferred constructor to use.
     * @param entity which was not found
     * @param reason why it was not found
     */
    public UpdateException(String entity, String reason) {
        super(String.format("Entity %s could not be updated. %s", entity, reason));
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
