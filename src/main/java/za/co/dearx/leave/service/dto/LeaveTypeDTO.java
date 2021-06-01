package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveType} entity.
 */
@ApiModel(
    description = "A type of leave supported by the system.\nEach type can be linked to a business process, which is invoked when a new application is created."
)
public class LeaveTypeDTO implements Serializable {

    private Long id;

    /**
     * A unique name for the leave type.
     */
    @NotNull
    @Size(max = 50)
    @ApiModelProperty(value = "A unique name for the leave type.", required = true)
    private String name;

    @Size(max = 200)
    private String description;

    /**
     * The process ID to invoke for a new leave application.
     */
    @Size(max = 200)
    @ApiModelProperty(value = "The process ID to invoke for a new leave application.")
    private String processName;

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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveTypeDTO)) {
            return false;
        }

        LeaveTypeDTO leaveTypeDTO = (LeaveTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", processName='" + getProcessName() + "'" +
            "}";
    }
}
