package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;

/**
 * Mapper for the entity {@link EntitlementValue} and its DTO {@link EntitlementValueDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveEntitlementMapper.class, StaffMapper.class })
public interface EntitlementValueMapper extends EntityMapper<EntitlementValueDTO, EntitlementValue> {
    @Mapping(source = "entitlement.id", target = "entitlementId")
    @Mapping(source = "staff.id", target = "staffId")
    @Mapping(source = "staff.name", target = "staffName")
    EntitlementValueDTO toDto(EntitlementValue entitlementValue);

    @Mapping(source = "entitlementId", target = "entitlement")
    @Mapping(source = "staffId", target = "staff")
    EntitlementValue toEntity(EntitlementValueDTO entitlementValueDTO);

    default EntitlementValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        EntitlementValue entitlementValue = new EntitlementValue();
        entitlementValue.setId(id);
        return entitlementValue;
    }
}
