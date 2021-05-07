package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;

/**
 * Mapper for the entity {@link LeaveApplication} and its DTO {@link LeaveApplicationDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveTypeMapper.class, LeaveStatusMapper.class, StaffMapper.class })
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {
    @Mapping(source = "leaveType.id", target = "leaveTypeId")
    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    @Mapping(source = "leaveStatus.id", target = "leaveStatusId")
    @Mapping(source = "leaveStatus.name", target = "leaveStatusName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.name", target = "staffName")
    LeaveApplicationDTO toDto(LeaveApplication leaveApplication);

    @Mapping(source = "leaveTypeId", target = "leaveType")
    @Mapping(source = "leaveStatusId", target = "leaveStatus")
    @Mapping(source = "staffId", target = "staff")
    LeaveApplication toEntity(LeaveApplicationDTO leaveApplicationDTO);

    default LeaveApplication fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setId(id);
        return leaveApplication;
    }
}
