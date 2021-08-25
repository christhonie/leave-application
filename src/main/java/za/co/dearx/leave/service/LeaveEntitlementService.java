package za.co.dearx.leave.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;
import za.co.dearx.leave.service.mapper.LeaveEntitlementMapper;

/**
 * Service Implementation for managing {@link LeaveEntitlement}.
 */
@Service
@Transactional
public class LeaveEntitlementService {

    private final Logger log = LoggerFactory.getLogger(LeaveEntitlementService.class);

    private final LeaveEntitlementRepository leaveEntitlementRepository;

    private final LeaveEntitlementMapper leaveEntitlementMapper;

    public LeaveEntitlementService(LeaveEntitlementRepository leaveEntitlementRepository, LeaveEntitlementMapper leaveEntitlementMapper) {
        this.leaveEntitlementRepository = leaveEntitlementRepository;
        this.leaveEntitlementMapper = leaveEntitlementMapper;
    }

    /**
     * Save a leaveEntitlement.
     *
     * @param leaveEntitlementDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveEntitlementDTO save(LeaveEntitlementDTO leaveEntitlementDTO) {
        log.debug("Request to save LeaveEntitlement : {}", leaveEntitlementDTO);
        LeaveEntitlement leaveEntitlement = leaveEntitlementMapper.toEntity(leaveEntitlementDTO);
        leaveEntitlement = leaveEntitlementRepository.save(leaveEntitlement);
        return leaveEntitlementMapper.toDto(leaveEntitlement);
    }

    /**
     * Partially update a leaveEntitlement.
     *
     * @param leaveEntitlementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeaveEntitlementDTO> partialUpdate(LeaveEntitlementDTO leaveEntitlementDTO) {
        log.debug("Request to partially update LeaveEntitlement : {}", leaveEntitlementDTO);

        return leaveEntitlementRepository
            .findById(leaveEntitlementDTO.getId())
            .map(
                existingLeaveEntitlement -> {
                    leaveEntitlementMapper.partialUpdate(existingLeaveEntitlement, leaveEntitlementDTO);
                    return existingLeaveEntitlement;
                }
            )
            .map(leaveEntitlementRepository::save)
            .map(leaveEntitlementMapper::toDto);
    }

    /**
     * Get all the leaveEntitlements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveEntitlementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveEntitlements");
        return leaveEntitlementRepository.findAll(pageable).map(leaveEntitlementMapper::toDto);
    }

    /**
     * Get one leaveEntitlement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveEntitlementDTO> findOne(Long id) {
        log.debug("Request to get LeaveEntitlement : {}", id);
        return leaveEntitlementRepository.findById(id).map(leaveEntitlementMapper::toDto);
    }

    /**
     * Delete the leaveEntitlement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveEntitlement : {}", id);
        leaveEntitlementRepository.deleteById(id);
    }

    /**
     * Apply {@link LeaveEntitlement}s to all {@link Staff} members of all {@link LeaveType}s.
     */
    public void apply(LocalDate date) {
        List<Staff> staff = new ArrayList<>(); //TODO Loop through all active staff. This is only a placeholder.
        List<LeaveType> types = new ArrayList<>(); //TODO Loop through all available ones. This is only a placeholder.
        types.forEach(
            type -> {
                //Find the relevant strategy
                //This uses the following patterns:
                // * Factory Pattern - https://springframework.guru/gang-of-four-design-patterns/factory-method-design-pattern/
                // * Strategy Pattern - https://springframework.guru/gang-of-four-design-patterns/strategy-pattern/
                LeaveEntitlementStrategyFactory
                    .create(type, this.leaveEntitlementRepository)
                    .ifPresent(
                        strategy -> {
                            staff.forEach(
                                s -> {
                                    strategy.apply(s, date);
                                }
                            );
                        }
                    );
            }
        );
    }
}
