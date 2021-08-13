package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.LeaveDeduction} entity.
 */
@ApiModel(
    description = "A mechanism to track leave deducted from one or more LeaveEntitlements for each LeaveApplication, which helps track how much, if any, leave is still remaining."
)
public class LeaveDeductionDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal days;

    private LeaveApplicationDTO application;

    private LeaveEntitlementDTO entitlement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public LeaveApplicationDTO getApplication() {
        return application;
    }

    public void setApplication(LeaveApplicationDTO application) {
        this.application = application;
    }

    public LeaveEntitlementDTO getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(LeaveEntitlementDTO entitlement) {
        this.entitlement = entitlement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeaveDeductionDTO)) {
            return false;
        }

        LeaveDeductionDTO leaveDeductionDTO = (LeaveDeductionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveDeductionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveDeductionDTO{" +
            "id=" + getId() +
            ", days=" + getDays() +
            ", application=" + getApplication() +
            ", entitlement=" + getEntitlement() +
            "}";
    }
}
