package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;

/**
 * Mapper for the entity {@link LeaveStatus} and its DTO {@link LeaveStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveStatusMapper extends EntityMapper<LeaveStatusDTO, LeaveStatus> {
    default LeaveStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveStatus leaveStatus = new LeaveStatus();
        leaveStatus.setId(id);
        return leaveStatus;
    }
}
