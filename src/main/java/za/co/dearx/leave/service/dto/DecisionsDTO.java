package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.Instant;
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

    /**
     * Optional comment DTO
     */
    @ApiModelProperty(value = "Optional comment DTO")
    private Long commentId;

    private String commentComment;

    private Long userId;

    private String userLogin;

    private Long leaveApplicationId;

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

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getCommentComment() {
        return commentComment;
    }

    public void setCommentComment(String commentComment) {
        this.commentComment = commentComment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(Long leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionsDTO)) {
            return false;
        }

        return id != null && id.equals(((DecisionsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionsDTO{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", decidedOn='" + getDecidedOn() + "'" +
            ", commentId=" + getCommentId() +
            ", commentComment='" + getCommentComment() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", leaveApplicationId=" + getLeaveApplicationId() +
            "}";
    }
}
