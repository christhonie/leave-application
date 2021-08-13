package za.co.dearx.leave.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.BooleanFilter;
import za.co.dearx.leave.domain.*; // for static metamodels
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.criteria.LeaveApplicationCriteria;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.mapper.LeaveApplicationMapper;

/**
 * Service for executing complex queries for {@link LeaveApplication} entities in the database.
 * The main input is a {@link LeaveApplicationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveApplicationDTO} or a {@link Page} of {@link LeaveApplicationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveApplicationQueryService extends QueryService<LeaveApplication> {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationQueryService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;

    private final LeaveApplicationMapper leaveApplicationMapper;

    public LeaveApplicationQueryService(
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveApplicationMapper leaveApplicationMapper
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveApplicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveApplicationDTO> findByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationMapper.toDto(leaveApplicationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveApplicationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveApplicationDTO> findByCriteria(LeaveApplicationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.findAll(specification, page).map(leaveApplicationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveApplicationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveApplication> specification = createSpecification(criteria);
        return leaveApplicationRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveApplicationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaveApplication> createSpecification(LeaveApplicationCriteria criteria) {
        Specification<LeaveApplication> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaveApplication_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), LeaveApplication_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), LeaveApplication_.endDate));
            }
            if (criteria.getAppliedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAppliedDate(), LeaveApplication_.appliedDate));
            }
            if (criteria.getUpdateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdateDate(), LeaveApplication_.updateDate));
            }
            if (criteria.getDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDays(), LeaveApplication_.days));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), LeaveApplication_.deleted));
            } else {
            	BooleanFilter deleted = new BooleanFilter();
            	deleted.setEquals(false);
            	specification = specification.and(buildSpecification(deleted, LeaveApplication_.deleted));
            }
            if (criteria.getLeaveTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaveTypeId(),
                            root -> root.join(LeaveApplication_.leaveType, JoinType.LEFT).get(LeaveType_.id)
                        )
                    );
            }
            if (criteria.getLeaveStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaveStatusId(),
                            root -> root.join(LeaveApplication_.leaveStatus, JoinType.LEFT).get(LeaveStatus_.id)
                        )
                    );
            }
            if (criteria.getStaffId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStaffId(), root -> root.join(LeaveApplication_.staff, JoinType.LEFT).get(Staff_.id))
                    );
            }
            if (criteria.getDeductionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeductionId(),
                            root -> root.join(LeaveApplication_.deductions, JoinType.LEFT).get(LeaveDeduction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
