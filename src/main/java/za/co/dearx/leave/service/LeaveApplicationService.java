package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.mapper.LeaveApplicationMapper;

/**
 * Service Implementation for managing {@link LeaveApplication}.
 */
@Service
@Transactional
public class LeaveApplicationService {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationService.class);

    private final LeaveApplicationRepository leaveApplicationRepository;

    private final LeaveApplicationMapper leaveApplicationMapper;

    public LeaveApplicationService(LeaveApplicationRepository leaveApplicationRepository, LeaveApplicationMapper leaveApplicationMapper) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
    }

    /**
     * Save a leaveApplication.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveApplicationDTO save(LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    /**
     * Partially update a leaveApplication.
     *
     * @param leaveApplicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeaveApplicationDTO> partialUpdate(LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to partially update LeaveApplication : {}", leaveApplicationDTO);

        return leaveApplicationRepository
            .findById(leaveApplicationDTO.getId())
            .map(
                existingLeaveApplication -> {
                    leaveApplicationMapper.partialUpdate(existingLeaveApplication, leaveApplicationDTO);
                    return existingLeaveApplication;
                }
            )
            .map(leaveApplicationRepository::save)
            .map(leaveApplicationMapper::toDto);
    }

    /**
     * Get all the leaveApplications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveApplicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveApplications");
        return leaveApplicationRepository.findAll(pageable).map(leaveApplicationMapper::toDto);
    }

    /**
     * Get one leaveApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveApplicationDTO> findOne(Long id) {
        log.debug("Request to get LeaveApplication : {}", id);
        return leaveApplicationRepository.findById(id).map(leaveApplicationMapper::toDto);
    }

    /**
     * Delete the leaveApplication by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveApplication : {}", id);
        leaveApplicationRepository.deleteById(id);
    }
}
