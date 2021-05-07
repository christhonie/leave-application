package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;

/**
 * Mapper for the entity {@link LeaveEntitlement} and its DTO {@link LeaveEntitlementDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveTypeMapper.class, StaffMapper.class })
public interface LeaveEntitlementMapper extends EntityMapper<LeaveEntitlementDTO, LeaveEntitlement> {
    @Mapping(target = "leaveType", source = "leaveType", qualifiedByName = "name")
    @Mapping(target = "staff", source = "staff", qualifiedByName = "name")
    LeaveEntitlementDTO toDto(LeaveEntitlement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeaveEntitlementDTO toDtoId(LeaveEntitlement leaveEntitlement);
}
