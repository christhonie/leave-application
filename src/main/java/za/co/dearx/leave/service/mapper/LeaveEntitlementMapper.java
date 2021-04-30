package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;

/**
 * Mapper for the entity {@link LeaveEntitlement} and its DTO {@link LeaveEntitlementDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveTypeMapper.class, StaffMapper.class })
public interface LeaveEntitlementMapper extends EntityMapper<LeaveEntitlementDTO, LeaveEntitlement> {
    @Mapping(source = "leaveType.id", target = "leaveTypeId")
    @Mapping(source = "leaveType.name", target = "leaveTypeName")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.name", target = "staffName")
    LeaveEntitlementDTO toDto(LeaveEntitlement leaveEntitlement);

    @Mapping(source = "leaveTypeId", target = "leaveType")
    @Mapping(source = "staffId", target = "staff")
    LeaveEntitlement toEntity(LeaveEntitlementDTO leaveEntitlementDTO);

    default LeaveEntitlement fromId(Long id) {
        if (id == null) {
            return null;
        }
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement();
        leaveEntitlement.setId(id);
        return leaveEntitlement;
    }
}
