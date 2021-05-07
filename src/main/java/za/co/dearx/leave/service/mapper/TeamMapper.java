package za.co.dearx.leave.service.mapper;

import java.util.Set;
import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.TeamDTO;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Mapping(target = "manager", source = "manager", qualifiedByName = "login")
    TeamDTO toDto(Team s);

    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<TeamDTO> toDtoNameSet(Set<Team> team);
}
