package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.RoleDTO;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(target = "users", source = "users", qualifiedByName = "loginSet")
    RoleDTO toDto(Role s);

    @Mapping(target = "removeUser", ignore = true)
    Role toEntity(RoleDTO roleDTO);
}
