package za.co.dearx.leave.bpmn;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import za.co.dearx.leave.bpmn.exception.NoMessageCatchException;
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;
import za.co.dearx.leave.service.exception.ValidationException;

@Service
public class MessageHander {

    /**
     * Message to indicate that the LeaveApplication was updated.
     */
    private static final String MESSAGE_UPDATE = "Update";
    /**
     * Message to signal an APPROVE or REJECT decision which has been made.
     */
    private static final String MESSAGE_DECISION = "Decision";
    /**
     * Message to signal a WITHDRAW decision which has been made.
     */
    private static final String MESSAGE_WITHDRAW = "Withdraw";
    /**
     * Message to indicate that the LeaveApplication is cancelled.
     */
    private static final String MESSAGE_CANCEL = "Cancel";

    /**
     * Process variable set to the choice which was made.
     */
    private static final String VARIABLE_CHOICE = "choice";

    private final RuntimeService runtimeService;

    public MessageHander(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    /**
     * Ensures the {@link LeaveApplication} parameter has valid values. This includes;
     * <ul>
     * <li>LeaveApplication itself is not null</li>
     * <li>LeaveApplication ID is not null</li>
     * </ul>
     * @param leaveApplication to be validated
     * @throws ValidationException indicating which validation failed.
     */
    private void checkValidLeaveApplication(LeaveApplication leaveApplication) throws ValidationException {
        if (leaveApplication == null) {
            throw new ValidationException("LeaveApplication", "The entity is null.");
        }
        if (leaveApplication.getId() == null) {
            throw new ValidationException(
                "LeaveApplication",
                "ID cannot be null. Was the leave application saved before starting the process?"
            );
        }
    }

    private boolean isValidLeaveType(LeaveApplication leaveApplication) throws ValidationException {
        if (leaveApplication.getLeaveType() == null) {
            throw new ValidationException("LeaveApplication", "LeaveType is null.");
        }
        if (leaveApplication.getLeaveType().getProcessName() == null || leaveApplication.getLeaveType().getProcessName().isBlank()) {
            //No process defined. Nothing to start. No need for exception.
            return false;
        }
        return true;
    }

    /**
     * Ensures the {@link Decisions} parameter has valid values. This includes;
     * <ul>
     * <li>Decision itself is not null</li>
     * <li>LeaveApplication is valid. See {@link #checkValidLeaveApplication(LeaveApplication).}
     * <li>Decision Choice is not null</li>
     * </ul>
     * @param decision to be validated
     * @throws ValidationException indicating which validation failed.
     */
    private void checkValidDecision(Decisions decision) throws ValidationException {
        if (decision == null) {
            throw new ValidationException("Decision", "The entity is null.");
        }
        checkValidLeaveApplication(decision.getLeaveApplication());
        if (decision.getChoice() == null) {
            throw new ValidationException("Decision", "Choice cannot be null.");
        }
    }

    /**
     * Start a new process for the given leave application.
     * If an existing process was already created before it will be re-used and the {@link #SIGNAL_UPDATE} signal will be sent.
     * @param leaveApplication to be associated with the process.
     * @return TRUE if a new process was created or FALSE if the process already existed.
     * @throws ValidationException if any of the required fields failed validation.
     * See {@link #checkValidLeaveApplication(LeaveApplication)} and {@link #isValidLeaveType(LeaveApplication)}
     */
    public boolean processLeaveApplication(LeaveApplication leaveApplication) throws ValidationException {
        //Validate that all required properties are set
        checkValidLeaveApplication(leaveApplication);
        if (isValidLeaveType(leaveApplication)) {
            //Check if there are any existing processes
            if (runtimeService.createExecutionQuery().processInstanceBusinessKey(leaveApplication.getId().toString()).count() == 0) {
                //Create an instance
                //The leaveType.processName is used to match the process key
                //The leaveApplication.id is used as business key
                runtimeService.startProcessInstanceByKey(
                    leaveApplication.getLeaveType().getProcessName(),
                    leaveApplication.getId().toString()
                );
                return true;
            } else {
                //Process instance already exist.
                //Send the UPDATE message
                runtimeService
                    .createMessageCorrelation(MESSAGE_UPDATE)
                    .processInstanceBusinessKey(leaveApplication.getId().toString())
                    .correlateAll();
            }
        }
        return false;
    }

    /**
     * Process a {@link Decisions} for a {@link LeaveApplication}.
     * @param decision to process.
     * @throws ValidationException if any of the required fields failed validation.
     * See {@link #checkValidDecision(Decisions)}.
     * @throws NoMessageCatchException if the process either does not have such a message,
     * or that the process was not waiting for such a message.
     */
    public void processDecicion(Decisions decision) throws ValidationException, NoMessageCatchException {
        //Validate that all required properties are set
        checkValidDecision(decision);
        try {
            //Correlate the business key and message to an instance
            //Set the CHOICE variable
            if (decision.getChoice() != DecisionChoice.WITHDRAW) {
                runtimeService
                    .createMessageCorrelation(MESSAGE_DECISION)
                    .processInstanceBusinessKey(decision.getLeaveApplication().getId().toString())
                    .setVariable(VARIABLE_CHOICE, decision.getChoice().toString())
                    .correlateAll();
            } else {
                runtimeService
                    .createMessageCorrelation(MESSAGE_WITHDRAW)
                    .processInstanceBusinessKey(decision.getLeaveApplication().getId().toString())
                    .correlateAll();
            }
        } catch (MismatchingMessageCorrelationException e) {
            //Process was not found, or there might have been multiple instance (which should not normally happen)
            //Raise an exception to indicate this.
            String processName = "Unknown";
            if (decision.getLeaveApplication().getLeaveType() != null) {
                processName = decision.getLeaveApplication().getLeaveType().getProcessName();
            }
            throw new NoMessageCatchException(processName, decision.getLeaveApplication().getId().toString(), MESSAGE_DECISION);
        }
    }

    public void cancelProcess(LeaveApplication leaveApplication) throws NoMessageCatchException {
        try {
            runtimeService
                .createMessageCorrelation(MESSAGE_CANCEL)
                .processInstanceBusinessKey(leaveApplication.getId().toString())
                .correlateWithResult();
        } catch (MismatchingMessageCorrelationException e) {
            //Process was not found, or there might have been multiple instance (which should not normally happen)
            //Raise an exception to indicate this.
            String processName = "Unknown";
            if (leaveApplication.getLeaveType() != null) {
                processName = leaveApplication.getLeaveType().getProcessName();
            }
            throw new NoMessageCatchException(processName, leaveApplication.getId().toString(), MESSAGE_CANCEL);
        }
    }
}
