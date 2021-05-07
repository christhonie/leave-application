package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.TeamDTO;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Mapping(source = "manager.id", target = "managerId")
    @Mapping(source = "manager.login", target = "managerLogin")
    TeamDTO toDto(Team team);

    @Mapping(source = "managerId", target = "manager")
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "removeMember", ignore = true)
    Team toEntity(TeamDTO teamDTO);

    default Team fromId(Long id) {
        if (id == null) {
            return null;
        }
        Team team = new Team();
        team.setId(id);
        return team;
    }
}
