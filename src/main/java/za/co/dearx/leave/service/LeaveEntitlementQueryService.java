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
import za.co.dearx.leave.domain.*; // for static metamodels
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;
import za.co.dearx.leave.service.criteria.LeaveEntitlementCriteria;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;
import za.co.dearx.leave.service.mapper.LeaveEntitlementMapper;

/**
 * Service for executing complex queries for {@link LeaveEntitlement} entities in the database.
 * The main input is a {@link LeaveEntitlementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveEntitlementDTO} or a {@link Page} of {@link LeaveEntitlementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveEntitlementQueryService extends QueryService<LeaveEntitlement> {

    private final Logger log = LoggerFactory.getLogger(LeaveEntitlementQueryService.class);

    private final LeaveEntitlementRepository leaveEntitlementRepository;

    private final LeaveEntitlementMapper leaveEntitlementMapper;

    public LeaveEntitlementQueryService(
        LeaveEntitlementRepository leaveEntitlementRepository,
        LeaveEntitlementMapper leaveEntitlementMapper
    ) {
        this.leaveEntitlementRepository = leaveEntitlementRepository;
        this.leaveEntitlementMapper = leaveEntitlementMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveEntitlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveEntitlementDTO> findByCriteria(LeaveEntitlementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveEntitlement> specification = createSpecification(criteria);
        return leaveEntitlementMapper.toDto(leaveEntitlementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveEntitlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveEntitlementDTO> findByCriteria(LeaveEntitlementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveEntitlement> specification = createSpecification(criteria);
        return leaveEntitlementRepository.findAll(specification, page).map(leaveEntitlementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveEntitlementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveEntitlement> specification = createSpecification(criteria);
        return leaveEntitlementRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveEntitlementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaveEntitlement> createSpecification(LeaveEntitlementCriteria criteria) {
        Specification<LeaveEntitlement> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaveEntitlement_.id));
            }
            if (criteria.getEntitlementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEntitlementDate(), LeaveEntitlement_.entitlementDate));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), LeaveEntitlement_.expiryDate));
            }
            if (criteria.getDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDays(), LeaveEntitlement_.days));
            }
            if (criteria.getLeaveTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaveTypeId(),
                            root -> root.join(LeaveEntitlement_.leaveType, JoinType.LEFT).get(LeaveType_.id)
                        )
                    );
            }
            if (criteria.getStaffId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStaffId(), root -> root.join(LeaveEntitlement_.staff, JoinType.LEFT).get(Staff_.id))
                    );
            }
            if (criteria.getDeductionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeductionId(),
                            root -> root.join(LeaveEntitlement_.deductions, JoinType.LEFT).get(LeaveDeduction_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
