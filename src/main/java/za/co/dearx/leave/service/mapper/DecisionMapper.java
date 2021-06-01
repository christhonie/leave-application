package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.DecisionDTO;

/**
 * Mapper for the entity {@link Decision} and its DTO {@link DecisionDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, UserMapper.class, LeaveApplicationMapper.class })
public interface DecisionMapper extends EntityMapper<DecisionDTO, Decision> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "comment")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "leaveApplication", source = "leaveApplication", qualifiedByName = "id")
    DecisionDTO toDto(Decision s);
}
