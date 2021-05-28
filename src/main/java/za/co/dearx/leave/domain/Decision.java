package za.co.dearx.leave.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;

/**
 * A Decision.
 */
@Entity
@Table(name = "decision")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Decision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "choice", nullable = false)
    private DecisionChoice choice;

    @NotNull
    @Column(name = "decided_on", nullable = false)
    private Instant decidedOn;

    /**
     * Optional comment DTO
     */
    @JsonIgnoreProperties(value = { "leaveApplication", "user" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Comment comment;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "leaveType", "leaveStatus", "staff" }, allowSetters = true)
    private LeaveApplication leaveApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Decision id(Long id) {
        this.id = id;
        return this;
    }

    public DecisionChoice getChoice() {
        return this.choice;
    }

    public Decision choice(DecisionChoice choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(DecisionChoice choice) {
        this.choice = choice;
    }

    public Instant getDecidedOn() {
        return this.decidedOn;
    }

    public Decision decidedOn(Instant decidedOn) {
        this.decidedOn = decidedOn;
        return this;
    }

    public void setDecidedOn(Instant decidedOn) {
        this.decidedOn = decidedOn;
    }

    public Comment getComment() {
        return this.comment;
    }

    public Decision comment(Comment comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return this.user;
    }

    public Decision user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveApplication getLeaveApplication() {
        return this.leaveApplication;
    }

    public Decision leaveApplication(LeaveApplication leaveApplication) {
        this.setLeaveApplication(leaveApplication);
        return this;
    }

    public void setLeaveApplication(LeaveApplication leaveApplication) {
        this.leaveApplication = leaveApplication;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Decision)) {
            return false;
        }
        return id != null && id.equals(((Decision) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Decision{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", decidedOn='" + getDecidedOn() + "'" +
            "}";
    }
}
