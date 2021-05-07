package za.co.dearx.leave.service;

import java.util.Optional;
import javax.validation.Valid;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.exception.NotFoundException;
import za.co.dearx.leave.service.exception.ValidationException;
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

    private final LeaveStatusService leaveStatusService;

    private final LeaveTypeService leaveTypeService;

    private final RuntimeService runtimeService;

    public LeaveApplicationService(
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveApplicationMapper leaveApplicationMapper,
        LeaveStatusService leaveStatusService,
        LeaveTypeService leaveTypeService,
        RuntimeService runtimeService
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.leaveStatusService = leaveStatusService;
        this.runtimeService = runtimeService;
        this.leaveTypeService = leaveTypeService;
    }

    /**
     * Create a leaveApplication as part of normal CRUD operations.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     * @throws ValidationException
     */
    public LeaveApplicationDTO create(LeaveApplicationDTO leaveApplicationDTO) throws ValidationException {
        log.debug("Request to create LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);

        boolean newProcess = false;
        if (leaveApplication.getId() == null) {
            newProcess = true;
            if (leaveApplication.getLeaveStatus() == null) {
                try {
                    leaveApplication.setLeaveStatus(leaveStatusService.getDraft());
                } catch (NotFoundException e) {
                    throw new ValidationException("LeaveApplicationDTO", "Status was null and default state cannot be found.");
                }
            }
        }

        leaveApplication = leaveApplicationRepository.save(leaveApplication);

        //When mapping a new LeaveApplication the LeaveType will not be fully set. Retrieve full record
        LeaveType leaveType = leaveTypeService.findEntityById(leaveApplication.getLeaveType().getId()).orElse(null);

        //Start a new business process, if defined
        if (newProcess && leaveType != null && leaveType.getProcessName() != null) {
            runtimeService.startProcessInstanceByKey(leaveType.getProcessName(), leaveApplication.getId().toString());
        }

        return leaveApplicationMapper.toDto(leaveApplication);
    }

    /**
     * Save a leaveApplication as part of a new process.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     * @throws NotFoundException
     */
    public LeaveApplicationDTO save(@Valid LeaveApplicationDTO leaveApplicationDTO) throws ValidationException {
        log.debug("Request to save LeaveApplication : {}", leaveApplicationDTO);
        LeaveApplication leaveApplication = leaveApplicationMapper.toEntity(leaveApplicationDTO);

        boolean newProcess = false;
        if (leaveApplication.getId() == null) {
            newProcess = true;
            if (leaveApplication.getLeaveStatus() == null) {
                try {
                    leaveApplication.setLeaveStatus(leaveStatusService.getDraft());
                } catch (NotFoundException e) {
                    throw new ValidationException("LeaveApplicationDTO", "Status was null and default state cannot be found.");
                }
            }
        }

        leaveApplication = leaveApplicationRepository.save(leaveApplication);

        //Start a new business process, if defined
        if (newProcess && leaveApplication.getLeaveType().getProcessName() != null) {
            runtimeService.startProcessInstanceByKey(leaveApplication.getLeaveType().getProcessName());
        }

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
     * Update leaveApplication status.
     *
     * @param id the id of the entity.
     * @param status of the entity.
     * @return
     * @return the entity.
     * @throws NotFoundException
     */
    public LeaveApplication updateStatus(Long id, final String statusString) throws NotFoundException {
        log.debug("Update LeaveApplication status: {} {}", id, statusString);

        LeaveStatus status = leaveStatusService
            .findEntityByName(statusString)
            .orElseThrow(() -> new NotFoundException("LeaveStatus", "Lookup with name not found: " + statusString));

        return updateStatus(id, status);
    }

    /**
     * Update leaveApplication status.
     *
     * @param id the id of the entity.
     * @param status of the entity.
     * @return
     * @return the entity.
     * @throws NotFoundException
     */
    public LeaveApplication updateStatus(Long id, final LeaveStatus status) throws NotFoundException {
        log.debug("Update LeaveApplication status: {} {}", id, status);

        return leaveApplicationRepository
            .findById(id)
            .map(la -> la.leaveStatus(status))
            .map(leaveApplicationRepository::save)
            .orElseThrow(() -> new NotFoundException("LeaveApplication with ID not found: " + id));
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
