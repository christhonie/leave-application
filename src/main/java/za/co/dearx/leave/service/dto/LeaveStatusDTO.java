package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveStatus} entity.
 */
@ApiModel(
    description = "A possible status of the LeaveApplication. The system defines a number of default statuses which cannot be changed."
)
public class LeaveStatusDTO implements Serializable {

    private Long id;

    /**
     * A unique name for the status. This value is used in the BPMN process and should not be changed once created.
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(
        value = "A unique name for the status. This value is used in the BPMN process and should not be changed once created.",
        required = true
    )
    private String name;

    @Size(max = 200)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveStatusDTO)) {
            return false;
        }

        LeaveStatusDTO leaveStatusDTO = (LeaveStatusDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveStatusDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveStatusDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
