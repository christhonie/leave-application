package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;

/**
 * Mapper for the entity {@link LeaveApplication} and its DTO {@link LeaveApplicationDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveTypeMapper.class, LeaveStatusMapper.class, StaffMapper.class })
public interface LeaveApplicationMapper extends EntityMapper<LeaveApplicationDTO, LeaveApplication> {
    @Mapping(target = "leaveType", source = "leaveType", qualifiedByName = "name")
    @Mapping(target = "leaveStatus", source = "leaveStatus", qualifiedByName = "name")
    @Mapping(target = "staff", source = "staff", qualifiedByName = "name")
    LeaveApplicationDTO toDto(LeaveApplication s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeaveApplicationDTO toDtoId(LeaveApplication leaveApplication);

    @Mapping(target = "days", ignore = true)
    LeaveApplication toEntity(LeaveApplicationDTO dto);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "days", ignore = true)
    void partialUpdate(@MappingTarget LeaveApplication entity, LeaveApplicationDTO dto);
}
