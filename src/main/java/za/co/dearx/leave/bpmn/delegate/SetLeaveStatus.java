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
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.service.LeaveApplicationService;
import za.co.dearx.leave.service.LeaveStatusService;

/**
 * Delegate for a BPMN Service Task to set the {@link LeaveApplication#leaveStatus(za.co.dearx.leave.domain.LeaveStatus)}
 * @author Christhonie Geldenhuys
 *
 */
@Component
public class SetLeaveStatus implements JavaDelegate {
    private final Logger log = LoggerFactory.getLogger(SetLeaveStatus.class);

    @Autowired
    private BPMNUtil util;

    @Autowired
    private LeaveStatusService leaveStatusService;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    /**
     * Optional Input variable from BPMN Service Task.
     */
    private Expression status;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("Process Set Leave Status delegate.");

        BPMNUtil.logExecution(log, execution);
        BPMNUtil.logVariables(log, execution);

        Long applicationId = util.getBusinessKeyAsLong(execution);

        LeaveStatus newStatus = null;
        Map<String, Object> variables = execution.getVariables();
        if (variables.containsKey("status")) {
            newStatus = leaveStatusService.findEntityByName((String) variables.get("status")).orElse(null);
        }

        String processStatus = (String) execution.getVariable("status");
        if (newStatus == null && processStatus != null) {
            newStatus = leaveStatusService.findEntityByName(processStatus).orElse(null);
        }

        leaveApplicationService.updateStatus(applicationId, newStatus);
    }

    public Expression getStatus() {
        return status;
    }

    public void setStatus(Expression status) {
        this.status = status;
    }
}
