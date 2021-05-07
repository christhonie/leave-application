package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveTypeDTO;

/**
 * Mapper for the entity {@link LeaveType} and its DTO {@link LeaveTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaveTypeMapper extends EntityMapper<LeaveTypeDTO, LeaveType> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    LeaveTypeDTO toDtoName(LeaveType leaveType);
}
