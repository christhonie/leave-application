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
import za.co.dearx.leave.domain.EntitlementValue;
import za.co.dearx.leave.repository.EntitlementValueRepository;
import za.co.dearx.leave.service.criteria.EntitlementValueCriteria;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;
import za.co.dearx.leave.service.mapper.EntitlementValueMapper;

/**
 * Service for executing complex queries for {@link EntitlementValue} entities in the database.
 * The main input is a {@link EntitlementValueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EntitlementValueDTO} or a {@link Page} of {@link EntitlementValueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntitlementValueQueryService extends QueryService<EntitlementValue> {

    private final Logger log = LoggerFactory.getLogger(EntitlementValueQueryService.class);

    private final EntitlementValueRepository entitlementValueRepository;

    private final EntitlementValueMapper entitlementValueMapper;

    public EntitlementValueQueryService(
        EntitlementValueRepository entitlementValueRepository,
        EntitlementValueMapper entitlementValueMapper
    ) {
        this.entitlementValueRepository = entitlementValueRepository;
        this.entitlementValueMapper = entitlementValueMapper;
    }

    /**
     * Return a {@link List} of {@link EntitlementValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EntitlementValueDTO> findByCriteria(EntitlementValueCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EntitlementValue> specification = createSpecification(criteria);
        return entitlementValueMapper.toDto(entitlementValueRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EntitlementValueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EntitlementValueDTO> findByCriteria(EntitlementValueCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EntitlementValue> specification = createSpecification(criteria);
        return entitlementValueRepository.findAll(specification, page).map(entitlementValueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntitlementValueCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EntitlementValue> specification = createSpecification(criteria);
        return entitlementValueRepository.count(specification);
    }

    /**
     * Function to convert {@link EntitlementValueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EntitlementValue> createSpecification(EntitlementValueCriteria criteria) {
        Specification<EntitlementValue> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EntitlementValue_.id));
            }
            if (criteria.getEntitlementValue() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEntitlementValue(), EntitlementValue_.entitlementValue));
            }
            if (criteria.getEntitlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEntitlementId(),
                            root -> root.join(EntitlementValue_.entitlement, JoinType.LEFT).get(LeaveEntitlement_.id)
                        )
                    );
            }
            if (criteria.getStaffId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStaffId(), root -> root.join(EntitlementValue_.staff, JoinType.LEFT).get(Staff_.id))
                    );
            }
        }
        return specification;
    }
}
