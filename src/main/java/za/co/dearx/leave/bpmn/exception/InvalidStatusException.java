package za.co.dearx.leave.bpmn.exception;

public class InvalidStatusException extends Exception {

    private static final long serialVersionUID = 5619313227148538836L;

    private final String processName;
    private final String businessKey;
    private final String state;

    public InvalidStatusException(String processName, String businessKey, String state) {
        super(
            String.format(
                "Could not find or apply state %s for LeaveApplication with key %s in process %s",
                state,
                businessKey,
                processName
            )
        );
        this.processName = processName;
        this.businessKey = businessKey;
        this.state = state;
    }

    public String getProcessName() {
        return processName;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getS() {
        return state;
    }
}
