package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;

/**
 * Mapper for the entity {@link EntitlementValue} and its DTO {@link EntitlementValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveEntitlementMapper.class, StaffMapper.class })
public interface EntitlementValueMapper extends EntityMapper<EntitlementValueDTO, EntitlementValue> {
    @Mapping(target = "entitlement", source = "entitlement", qualifiedByName = "id")
    @Mapping(target = "staff", source = "staff", qualifiedByName = "name")
    EntitlementValueDTO toDto(EntitlementValue s);
}
