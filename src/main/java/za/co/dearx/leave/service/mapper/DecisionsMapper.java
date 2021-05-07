package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.DecisionsDTO;

/**
 * Mapper for the entity {@link Decisions} and its DTO {@link DecisionsDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, UserMapper.class, LeaveApplicationMapper.class })
public interface DecisionsMapper extends EntityMapper<DecisionsDTO, Decisions> {
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "comment.comment", target = "commentComment")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "leaveApplication.id", target = "leaveApplicationId")
    DecisionsDTO toDto(Decisions decisions);

    @Mapping(source = "commentId", target = "comment")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "leaveApplicationId", target = "leaveApplication")
    Decisions toEntity(DecisionsDTO decisionsDTO);

    default Decisions fromId(Long id) {
        if (id == null) {
            return null;
        }
        Decisions decisions = new Decisions();
        decisions.setId(id);
        return decisions;
    }
}
