package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveDeductionDTO;

/**
 * Mapper for the entity {@link LeaveDeduction} and its DTO {@link LeaveDeductionDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveApplicationMapper.class, LeaveEntitlementMapper.class })
public interface LeaveDeductionMapper extends EntityMapper<LeaveDeductionDTO, LeaveDeduction> {
    @Mapping(target = "application", source = "application", qualifiedByName = "id")
    @Mapping(target = "entitlement", source = "entitlement", qualifiedByName = "id")
    LeaveDeductionDTO toDto(LeaveDeduction s);
}
