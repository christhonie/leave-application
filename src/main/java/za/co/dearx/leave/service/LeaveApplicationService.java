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
import za.co.dearx.leave.bpmn.MessageHander;
import za.co.dearx.leave.bpmn.exception.NoMessageCatchException;
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

    private final MessageHander messageHandler;

    public LeaveApplicationService(
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveApplicationMapper leaveApplicationMapper,
        LeaveStatusService leaveStatusService,
        LeaveTypeService leaveTypeService,
        MessageHander messageHandler
    ) {
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationMapper = leaveApplicationMapper;
        this.leaveStatusService = leaveStatusService;
        this.leaveTypeService = leaveTypeService;
        this.messageHandler = messageHandler;
    }

    /**
     * Create a leaveApplication as part of normal CRUD operations.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     * @throws ValidationException when no LeaveStatus was specified and there is also no default LeaveStatus defined.
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
        leaveApplication.setLeaveType(leaveType);

        //Start a new business process, if defined
        if (newProcess && leaveType != null && leaveType.getProcessName() != null) {
            messageHandler.processLeaveApplication(leaveApplication);
        }

        return leaveApplicationMapper.toDto(leaveApplication);
    }

    /**
     * Save a leaveApplication as part of a new process.
     *
     * @param leaveApplicationDTO the entity to save.
     * @return the persisted entity.
     * @throws ValidationException when no LeaveStatus was specified and there is also no default LeaveStatus defined.
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
        if (newProcess && leaveApplication.getLeaveType().getProcessName() != null) {}

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
     * Get one leaveApplication by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveApplication> findOneEntity(Long id) {
        log.debug("Request to get LeaveApplication : {}", id);
        return leaveApplicationRepository.findById(id);
    }

    /**
     * Update leaveApplication status.
     *
     * @param id the id of the entity.
     * @param statusName is the name of the LeaveStatus.
     * @return the updated LeaveApplication
     * @throws NotFoundException when the LeaveStatus with the statusName could not be found.
     */
    public LeaveApplication updateStatus(Long id, final String statusName) throws NotFoundException {
        log.debug("Update LeaveApplication status: {} {}", id, statusName);

        LeaveStatus status = leaveStatusService
            .findEntityByName(statusName)
            .orElseThrow(() -> new NotFoundException("LeaveStatus", "Lookup with name not found: " + statusName));

        return updateStatus(id, status);
    }

    /**
     * Update leaveApplication status.
     *
     * @param id the id of the entity.
     * @param status to apply to the {@link LeaveApplication}
     * @return the updated LeaveApplication with ID entity.
     * @throws NotFoundException when the LeaveApplication with ID with the given id could not be found.
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
                    record.setDeleted(true);
                    leaveApplicationRepository.save(record);
                    try {
                        messageHandler.cancelProcess(record);
                    } catch (NoMessageCatchException e) {
                        //Nothing to do
                    }
                }
            );
    }
}
