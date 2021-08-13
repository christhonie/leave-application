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
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.repository.LeaveDeductionRepository;
import za.co.dearx.leave.service.criteria.LeaveDeductionCriteria;
import za.co.dearx.leave.service.dto.LeaveDeductionDTO;
import za.co.dearx.leave.service.mapper.LeaveDeductionMapper;

/**
 * Service for executing complex queries for {@link LeaveDeduction} entities in the database.
 * The main input is a {@link LeaveDeductionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveDeductionDTO} or a {@link Page} of {@link LeaveDeductionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveDeductionQueryService extends QueryService<LeaveDeduction> {

    private final Logger log = LoggerFactory.getLogger(LeaveDeductionQueryService.class);

    private final LeaveDeductionRepository leaveDeductionRepository;

    private final LeaveDeductionMapper leaveDeductionMapper;

    public LeaveDeductionQueryService(LeaveDeductionRepository leaveDeductionRepository, LeaveDeductionMapper leaveDeductionMapper) {
        this.leaveDeductionRepository = leaveDeductionRepository;
        this.leaveDeductionMapper = leaveDeductionMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveDeductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveDeductionDTO> findByCriteria(LeaveDeductionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveDeduction> specification = createSpecification(criteria);
        return leaveDeductionMapper.toDto(leaveDeductionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveDeductionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveDeductionDTO> findByCriteria(LeaveDeductionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveDeduction> specification = createSpecification(criteria);
        return leaveDeductionRepository.findAll(specification, page).map(leaveDeductionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveDeductionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveDeduction> specification = createSpecification(criteria);
        return leaveDeductionRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveDeductionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaveDeduction> createSpecification(LeaveDeductionCriteria criteria) {
        Specification<LeaveDeduction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaveDeduction_.id));
            }
            if (criteria.getDays() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDays(), LeaveDeduction_.days));
            }
            if (criteria.getApplicationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationId(),
                            root -> root.join(LeaveDeduction_.application, JoinType.LEFT).get(LeaveApplication_.id)
                        )
                    );
            }
            if (criteria.getEntitlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEntitlementId(),
                            root -> root.join(LeaveDeduction_.entitlement, JoinType.LEFT).get(LeaveEntitlement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
