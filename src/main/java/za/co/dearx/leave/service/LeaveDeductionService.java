package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.repository.LeaveDeductionRepository;
import za.co.dearx.leave.service.dto.LeaveDeductionDTO;
import za.co.dearx.leave.service.mapper.LeaveDeductionMapper;

/**
 * Service Implementation for managing {@link LeaveDeduction}.
 */
@Service
@Transactional
public class LeaveDeductionService {

    private final Logger log = LoggerFactory.getLogger(LeaveDeductionService.class);

    private final LeaveDeductionRepository leaveDeductionRepository;

    private final LeaveDeductionMapper leaveDeductionMapper;

    public LeaveDeductionService(LeaveDeductionRepository leaveDeductionRepository, LeaveDeductionMapper leaveDeductionMapper) {
        this.leaveDeductionRepository = leaveDeductionRepository;
        this.leaveDeductionMapper = leaveDeductionMapper;
    }

    /**
     * Save a leaveDeduction.
     *
     * @param leaveDeductionDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveDeductionDTO save(LeaveDeductionDTO leaveDeductionDTO) {
        log.debug("Request to save LeaveDeduction : {}", leaveDeductionDTO);
        LeaveDeduction leaveDeduction = leaveDeductionMapper.toEntity(leaveDeductionDTO);
        leaveDeduction = leaveDeductionRepository.save(leaveDeduction);
        return leaveDeductionMapper.toDto(leaveDeduction);
    }

    /**
     * Partially update a leaveDeduction.
     *
     * @param leaveDeductionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeaveDeductionDTO> partialUpdate(LeaveDeductionDTO leaveDeductionDTO) {
        log.debug("Request to partially update LeaveDeduction : {}", leaveDeductionDTO);

        return leaveDeductionRepository
            .findById(leaveDeductionDTO.getId())
            .map(
                existingLeaveDeduction -> {
                    leaveDeductionMapper.partialUpdate(existingLeaveDeduction, leaveDeductionDTO);
                    return existingLeaveDeduction;
                }
            )
            .map(leaveDeductionRepository::save)
            .map(leaveDeductionMapper::toDto);
    }

    /**
     * Get all the leaveDeductions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveDeductionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveDeductions");
        return leaveDeductionRepository.findAll(pageable).map(leaveDeductionMapper::toDto);
    }

    /**
     * Get one leaveDeduction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveDeductionDTO> findOne(Long id) {
        log.debug("Request to get LeaveDeduction : {}", id);
        return leaveDeductionRepository.findById(id).map(leaveDeductionMapper::toDto);
    }

    /**
     * Delete the leaveDeduction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveDeduction : {}", id);
        leaveDeductionRepository.deleteById(id);
    }
}
