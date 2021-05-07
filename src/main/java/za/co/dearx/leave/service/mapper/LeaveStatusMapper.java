package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;

/**
 * Mapper for the entity {@link LeaveStatus} and its DTO {@link LeaveStatusDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveStatusMapper extends EntityMapper<LeaveStatusDTO, LeaveStatus> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LeaveStatusDTO toDtoName(LeaveStatus leaveStatus);
}
