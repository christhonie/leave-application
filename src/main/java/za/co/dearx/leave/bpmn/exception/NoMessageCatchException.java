package za.co.dearx.leave.bpmn.exception;

public class NoMessageCatchException extends Exception {

    private static final long serialVersionUID = -2104161303165857568L;

    private final String businessKey;
    private final String messageName;

    public NoMessageCatchException(String processName, String businessKey, String messageName) {
        super(String.format("Process with key %s did not catch message %s", businessKey, messageName));
        this.businessKey = businessKey;
        this.messageName = messageName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getMessageName() {
        return messageName;
    }
}
