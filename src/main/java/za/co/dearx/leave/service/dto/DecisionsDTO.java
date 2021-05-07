package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.Decisions} entity.
 */
public class DecisionsDTO implements Serializable {

    private Long id;

    @NotNull
    private DecisionChoice choice;

    @NotNull
    private Instant decidedOn;

    private CommentDTO comment;

    private UserDTO user;

    private LeaveApplicationDTO leaveApplication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DecisionChoice getChoice() {
        return choice;
    }

    public void setChoice(DecisionChoice choice) {
        this.choice = choice;
    }

    public Instant getDecidedOn() {
        return decidedOn;
    }

    public void setDecidedOn(Instant decidedOn) {
        this.decidedOn = decidedOn;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        this.comment = comment;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LeaveApplicationDTO getLeaveApplication() {
        return leaveApplication;
    }

    public void setLeaveApplication(LeaveApplicationDTO leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionsDTO)) {
            return false;
        }

        DecisionsDTO decisionsDTO = (DecisionsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, decisionsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionsDTO{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", decidedOn='" + getDecidedOn() + "'" +
            ", comment=" + getComment() +
            ", user=" + getUser() +
            ", leaveApplication=" + getLeaveApplication() +
            "}";
    }
}
