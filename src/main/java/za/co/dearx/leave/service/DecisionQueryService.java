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
import za.co.dearx.leave.domain.Decision;
import za.co.dearx.leave.repository.DecisionRepository;
import za.co.dearx.leave.service.criteria.DecisionCriteria;
import za.co.dearx.leave.service.dto.DecisionDTO;
import za.co.dearx.leave.service.mapper.DecisionMapper;

/**
 * Service for executing complex queries for {@link Decision} entities in the database.
 * The main input is a {@link DecisionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DecisionDTO} or a {@link Page} of {@link DecisionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecisionQueryService extends QueryService<Decision> {

    private final Logger log = LoggerFactory.getLogger(DecisionQueryService.class);

    private final DecisionRepository decisionRepository;

    private final DecisionMapper decisionMapper;

    public DecisionQueryService(DecisionRepository decisionRepository, DecisionMapper decisionMapper) {
        this.decisionRepository = decisionRepository;
        this.decisionMapper = decisionMapper;
    }

    /**
     * Return a {@link List} of {@link DecisionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DecisionDTO> findByCriteria(DecisionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Decision> specification = createSpecification(criteria);
        return decisionMapper.toDto(decisionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DecisionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionDTO> findByCriteria(DecisionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Decision> specification = createSpecification(criteria);
        return decisionRepository.findAll(specification, page).map(decisionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecisionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Decision> specification = createSpecification(criteria);
        return decisionRepository.count(specification);
    }

    /**
     * Function to convert {@link DecisionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Decision> createSpecification(DecisionCriteria criteria) {
        Specification<Decision> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Decision_.id));
            }
            if (criteria.getChoice() != null) {
                specification = specification.and(buildSpecification(criteria.getChoice(), Decision_.choice));
            }
            if (criteria.getDecidedOn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDecidedOn(), Decision_.decidedOn));
            }
            if (criteria.getCommentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCommentId(), root -> root.join(Decision_.comment, JoinType.LEFT).get(Comment_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Decision_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getLeaveApplicationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaveApplicationId(),
                            root -> root.join(Decision_.leaveApplication, JoinType.LEFT).get(LeaveApplication_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
