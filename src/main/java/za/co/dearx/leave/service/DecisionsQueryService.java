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
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.repository.DecisionsRepository;
import za.co.dearx.leave.service.criteria.DecisionsCriteria;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.service.mapper.DecisionsMapper;

/**
 * Service for executing complex queries for {@link Decisions} entities in the database.
 * The main input is a {@link DecisionsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DecisionsDTO} or a {@link Page} of {@link DecisionsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecisionsQueryService extends QueryService<Decisions> {

    private final Logger log = LoggerFactory.getLogger(DecisionsQueryService.class);

    private final DecisionsRepository decisionsRepository;

    private final DecisionsMapper decisionsMapper;

    public DecisionsQueryService(DecisionsRepository decisionsRepository, DecisionsMapper decisionsMapper) {
        this.decisionsRepository = decisionsRepository;
        this.decisionsMapper = decisionsMapper;
    }

    /**
     * Return a {@link List} of {@link DecisionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DecisionsDTO> findByCriteria(DecisionsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Decisions> specification = createSpecification(criteria);
        return decisionsMapper.toDto(decisionsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DecisionsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionsDTO> findByCriteria(DecisionsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Decisions> specification = createSpecification(criteria);
        return decisionsRepository.findAll(specification, page).map(decisionsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecisionsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Decisions> specification = createSpecification(criteria);
        return decisionsRepository.count(specification);
    }

    /**
     * Function to convert {@link DecisionsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Decisions> createSpecification(DecisionsCriteria criteria) {
        Specification<Decisions> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Decisions_.id));
            }
            if (criteria.getChoice() != null) {
                specification = specification.and(buildSpecification(criteria.getChoice(), Decisions_.choice));
            }
            if (criteria.getDecidedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecidedOn(), Decisions_.decidedOn));
            }
            if (criteria.getCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommentId(), root -> root.join(Decisions_.comment, JoinType.LEFT).get(Comment_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Decisions_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getLeaveApplicationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaveApplicationId(),
                            root -> root.join(Decisions_.leaveApplication, JoinType.LEFT).get(LeaveApplication_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
