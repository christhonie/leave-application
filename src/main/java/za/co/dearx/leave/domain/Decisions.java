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
 * A Decisions.
 */
@Entity
@Table(name = "decisions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Decisions implements Serializable {
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
    @OneToOne
    @JoinColumn(unique = true)
    private Comment comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "decisions", allowSetters = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "decisions", allowSetters = true)
    private LeaveApplication leaveApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DecisionChoice getChoice() {
        return choice;
    }

    public Decisions choice(DecisionChoice choice) {
        this.choice = choice;
        return this;
    }

    public void setChoice(DecisionChoice choice) {
        this.choice = choice;
    }

    public Instant getDecidedOn() {
        return decidedOn;
    }

    public Decisions decidedOn(Instant decidedOn) {
        this.decidedOn = decidedOn;
        return this;
    }

    public void setDecidedOn(Instant decidedOn) {
        this.decidedOn = decidedOn;
    }

    public Comment getComment() {
        return comment;
    }

    public Decisions comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Decisions user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveApplication getLeaveApplication() {
        return leaveApplication;
    }

    public Decisions leaveApplication(LeaveApplication leaveApplication) {
        this.leaveApplication = leaveApplication;
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
        if (!(o instanceof Decisions)) {
            return false;
        }
        return id != null && id.equals(((Decisions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Decisions{" +
            "id=" + getId() +
            ", choice='" + getChoice() + "'" +
            ", decidedOn='" + getDecidedOn() + "'" +
            "}";
    }
}
