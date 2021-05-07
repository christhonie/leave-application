package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.DecisionsDTO;

/**
 * Mapper for the entity {@link Decisions} and its DTO {@link DecisionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, UserMapper.class, LeaveApplicationMapper.class })
public interface DecisionsMapper extends EntityMapper<DecisionsDTO, Decisions> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "comment")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "leaveApplication", source = "leaveApplication", qualifiedByName = "id")
    DecisionsDTO toDto(Decisions s);
}
