package za.co.dearx.leave.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link za.co.dearx.leave.domain.PublicHoliday} entity.
 */
@ApiModel(description = "An observed public holiday represents a date in South Africa where the holiday causes a work day to be impacted.")
public class PublicHolidayDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate date;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublicHolidayDTO)) {
            return false;
        }

        PublicHolidayDTO publicHolidayDTO = (PublicHolidayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, publicHolidayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublicHolidayDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
