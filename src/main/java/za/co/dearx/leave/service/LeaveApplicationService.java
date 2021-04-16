package za.co.dearx.leave.service;

import java.util.Optional;
import javax.validation.Valid;
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
     * Create a leaveApplication as part of normal CRUD operations.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveApplicationDTO create(LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return leaveApplicationMapper.toDto(leaveApplication);
    }

    /**
     * Save a leaveApplication as part of a new process.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveApplicationDTO save(@Valid LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        leaveApplication.setLeaveStatus(LeaveStatusService.getDraft());
        leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return leaveApplicationMapper.toDto(leaveApplication);
        //TODO Make sure we cannot save a deleted record. Must throw exception.
    }

    /**
     * Submit a leaveApplication as part of a new (or saved) process.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveApplicationDTO submit(@Valid LeaveApplicationDTO leaveApplicationDTO) {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);
        //TODO Set this somewhere: leaveApplication.setLeaveStatus(LeaveStatusService.getDraft());
        leaveApplication = leaveApplicationRepository.save(leaveApplication);
        return leaveApplicationMapper.toDto(leaveApplication);
        //TODO Make sure we cannot save a deleted record. Must throw exception.
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
        //TODO Only return non-deleted records by default. Maybe this is done in QueryService
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
        leaveApplicationRepository
            .findById(id)
            .ifPresent(
                record -> {
                    //TODO Add after code regeneration
                    //record.setDeleted(true);
                    leaveApplicationRepository.save(record);
                }
            );
    }
}
