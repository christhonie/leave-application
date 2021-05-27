package za.co.dearx.leave.bpmn.delegate;

import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import za.co.dearx.leave.bpmn.BPMNUtil;
import za.co.dearx.leave.service.LeaveApplicationService;

/**
 * Delegate for a BPMN Service Task to notify a user.
 * @author Christhonie Geldenhuys
 *
 */
@Component
public class NotifyUser implements JavaDelegate {

    private final Logger log = LoggerFactory.getLogger(NotifyUser.class);

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    /**
     * Optional Input variable from BPMN Service Task.
     */
    private Expression status;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("Notify user delegate.");

        if (log.isDebugEnabled()) BPMNUtil.logExecution(log, execution);

        final String notification;
        Map<String, Object> variables = execution.getVariables();
        if (variables.containsKey("notificationType")) {
            notification = (String) variables.get("notificationType");
        } else {
            notification = "";
        }

        Long applicationId = BPMNUtil.getBusinessKeyAsLong(execution);
        leaveApplicationService
            .findOne(applicationId)
            .ifPresent(
                application -> {
                    log.debug("Notifying user " + application.getStaff().getName() + " via email at " + application.getStaff().getEmail());
                    log.debug("Your leave status is " + notification);
                    //TODO Send email
                }
            );
    }
}
