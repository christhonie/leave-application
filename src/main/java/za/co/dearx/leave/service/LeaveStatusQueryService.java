package za.co.dearx.leave.service;

import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.*; // for static metamodels
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.repository.LeaveStatusRepository;
import za.co.dearx.leave.service.dto.LeaveStatusCriteria;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;
import za.co.dearx.leave.service.mapper.LeaveStatusMapper;

/**
 * Service for executing complex queries for {@link LeaveStatus} entities in the database.
 * The main input is a {@link LeaveStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaveStatusDTO} or a {@link Page} of {@link LeaveStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaveStatusQueryService extends QueryService<LeaveStatus> {
    private final Logger log = LoggerFactory.getLogger(LeaveStatusQueryService.class);

    private final LeaveStatusRepository leaveStatusRepository;

    private final LeaveStatusMapper leaveStatusMapper;

    public LeaveStatusQueryService(LeaveStatusRepository leaveStatusRepository, LeaveStatusMapper leaveStatusMapper) {
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveStatusMapper = leaveStatusMapper;
    }

    /**
     * Return a {@link List} of {@link LeaveStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaveStatusDTO> findByCriteria(LeaveStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaveStatus> specification = createSpecification(criteria);
        return leaveStatusMapper.toDto(leaveStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaveStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveStatusDTO> findByCriteria(LeaveStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaveStatus> specification = createSpecification(criteria);
        return leaveStatusRepository.findAll(specification, page).map(leaveStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaveStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaveStatus> specification = createSpecification(criteria);
        return leaveStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaveStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaveStatus> createSpecification(LeaveStatusCriteria criteria) {
        Specification<LeaveStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaveStatus_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), LeaveStatus_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeaveStatus_.description));
            }
        }
        return specification;
    }
}
