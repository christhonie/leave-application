package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.Comment} entity.
 */
@ApiModel(description = "Comments assigned to a Decision or a LeaveApplication.")
public class CommentDTO implements Serializable {

    private Long id;

    /**
     * A free text message up to 5000 characters.
     */
    @NotNull
    @Size(max = 5000)
    @ApiModelProperty(value = "A free text message up to 5000 characters.", required = true)
    private String comment;

    private LeaveApplicationDTO leaveApplication;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LeaveApplicationDTO getLeaveApplication() {
        return leaveApplication;
    }

    public void setLeaveApplication(LeaveApplicationDTO leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", leaveApplication=" + getLeaveApplication() +
            ", user=" + getUser() +
            "}";
    }
}
