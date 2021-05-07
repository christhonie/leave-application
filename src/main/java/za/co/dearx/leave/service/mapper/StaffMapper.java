package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.StaffDTO;

/**
 * Mapper for the entity {@link Staff} and its DTO {@link StaffDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, TeamMapper.class })
public interface StaffMapper extends EntityMapper<StaffDTO, Staff> {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    StaffDTO toDto(Staff staff);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeTeam", ignore = true)
    Staff toEntity(StaffDTO staffDTO);

    default Staff fromId(Long id) {
        if (id == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setId(id);
        return staff;
    }
}
