package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.CommentDTO;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring", uses = { LeaveApplicationMapper.class, UserMapper.class })
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "leaveApplication", source = "leaveApplication", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    CommentDTO toDto(Comment s);

    @Named("comment")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "comment", source = "comment")
    CommentDTO toDtoComment(Comment comment);
}
