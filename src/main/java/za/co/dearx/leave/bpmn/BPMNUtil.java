package za.co.dearx.leave.bpmn;

import java.util.Map;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import za.co.dearx.leave.service.exception.NotFoundException;

public class BPMNUtil {

    public static Long getBusinessKeyAsLong(DelegateExecution execution) throws NotFoundException {
        try {
            return Long.parseUnsignedLong(execution.getProcessBusinessKey());
        } catch (NumberFormatException e) {
            throw new NotFoundException("Business key not a Long: " + execution.getProcessBusinessKey());
        }
    }

    public static void logExecution(Logger log, DelegateExecution execution) {
        log.debug(
            "LoggerDelegate invoked by " +
            "processDefinitionId=" +
            execution.getProcessDefinitionId() +
            ", activityId=" +
            execution.getCurrentActivityId() +
            ", activityName='" +
            execution.getCurrentActivityName().replaceAll("\n", " ") +
            "'" +
            ", processInstanceId=" +
            execution.getProcessInstanceId() +
            ", businessKey=" +
            execution.getProcessBusinessKey() +
            ", executionId=" +
            execution.getId() +
            ", modelName=" +
            execution.getBpmnModelInstance().getModel().getModelName() +
            ", elementId" +
            execution.getBpmnModelElementInstance().getId() +
            " \n\n"
        );
    }

    public static void logVariables(Logger log, DelegateExecution execution) {
        log.debug("Variables:");
        Map<String, Object> variables = execution.getVariables();
        for (Map.Entry<String, Object> entry : variables.entrySet()) log.debug(" " + entry.getKey() + " : " + entry.getValue());
    }
}
