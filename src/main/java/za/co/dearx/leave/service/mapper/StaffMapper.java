package za.co.dearx.leave.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.StaffDTO;

/**
 * Mapper for the entity {@link Staff} and its DTO {@link StaffDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, TeamMapper.class })
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "teams", source = "teams", qualifiedByName = "nameSet")
    StaffDTO toDto(Staff s);

    @Mapping(target = "removeTeam", ignore = true)
    Staff toEntity(StaffDTO staffDTO);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StaffDTO toDtoName(Staff staff);
}
