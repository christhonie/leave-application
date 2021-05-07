package za.co.dearx.leave.service.dto;

import java.io.Serializable;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.Team} entity.
 */
public class TeamDTO implements Serializable {
    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    private Long managerId;

    private String managerLogin;

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

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long userId) {
        this.managerId = userId;
    }

    public String getManagerLogin() {
        return managerLogin;
    }

    public void setManagerLogin(String userLogin) {
        this.managerLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamDTO)) {
            return false;
        }

        return id != null && id.equals(((TeamDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeamDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", managerId=" + getManagerId() +
            ", managerLogin='" + getManagerLogin() + "'" +
            "}";
    }
}
