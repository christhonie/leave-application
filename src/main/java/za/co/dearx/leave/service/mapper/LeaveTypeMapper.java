package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveTypeDTO;

/**
 * Mapper for the entity {@link LeaveType} and its DTO {@link LeaveTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveTypeMapper extends EntityMapper<LeaveTypeDTO, LeaveType> {
    default LeaveType fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveType leaveType = new LeaveType();
        leaveType.setId(id);
        return leaveType;
    }
}
