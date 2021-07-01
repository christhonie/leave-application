package za.co.dearx.leave.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.repository.LeaveStatusRepository;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;
import za.co.dearx.leave.service.exception.NotFoundException;
import za.co.dearx.leave.service.mapper.LeaveStatusMapper;

/**
 * Service Implementation for managing {@link LeaveStatus}.
 */
@Service
@Transactional
public class LeaveStatusService {

    private final Logger log = LoggerFactory.getLogger(LeaveStatusService.class);

    private final LeaveStatusRepository leaveStatusRepository;

    private final LeaveStatusMapper leaveStatusMapper;
    
    private final LeaveApplicationService leaveAppService;

    public LeaveStatusService(LeaveStatusRepository leaveStatusRepository, LeaveStatusMapper leaveStatusMapper, LeaveApplicationService leaveAppService) {
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveStatusMapper = leaveStatusMapper;
        this.leaveAppService = leaveAppService;
    }

    /**
     * Save a leaveStatus.
     *
     * @param leaveStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveStatusDTO save(LeaveStatusDTO leaveStatusDTO) {
        log.debug("Request to save LeaveStatus : {}", leaveStatusDTO);
        LeaveStatus leaveStatus = leaveStatusMapper.toEntity(leaveStatusDTO);
        leaveStatus = leaveStatusRepository.save(leaveStatus);
        return leaveStatusMapper.toDto(leaveStatus);
    }

    /**
     * Partially update a leaveStatus.
     *
     * @param leaveStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeaveStatusDTO> partialUpdate(LeaveStatusDTO leaveStatusDTO) {
        log.debug("Request to partially update LeaveStatus : {}", leaveStatusDTO);

        return leaveStatusRepository
            .findById(leaveStatusDTO.getId())
            .map(
                existingLeaveStatus -> {
                    leaveStatusMapper.partialUpdate(existingLeaveStatus, leaveStatusDTO);
                    return existingLeaveStatus;
                }
            )
            .map(leaveStatusRepository::save)
            .map(leaveStatusMapper::toDto);
    }

    /**
     * Get all the leaveStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveStatusDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveStatuses");
        return leaveStatusRepository.findAll(pageable).map(leaveStatusMapper::toDto);
    }

    /**
     * Get one leaveStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveStatusDTO> findOne(Long id) {
        log.debug("Request to get LeaveStatus : {}", id);
        return leaveStatusRepository.findById(id).map(leaveStatusMapper::toDto);
    }

    /**
     * Delete the leaveStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveStatus : {}", id);
        leaveStatusRepository.deleteById(id);
    }

    public Optional<LeaveStatus> findEntityByName(String statusString) {
        if (statusString == null || statusString.isEmpty()) return Optional.empty();

        return leaveStatusRepository.findByName(statusString);
    }

    public LeaveStatus getDraft() throws NotFoundException {
        return findEntityByName("Draft").orElseThrow(() -> new NotFoundException("LeaveStatus", "Draft status not found"));
    }

    @Scheduled(cron = "5 0 * * *")
    public void scheduleTaskUsingCronExpression() {
    	log.info("[Scheduled Task running to update Leave Statues]");
        LocalDate currentDate = LocalDate.now();
        
        Page<LeaveApplicationDTO> allLeaveApps = this.leaveAppService.findAll(null);
        List<LeaveApplicationDTO> leaveAppList = allLeaveApps.getContent();

        for (LeaveApplicationDTO leaveApp: leaveAppList) {
            if (leaveApp.getStartDate().isAfter(currentDate) && leaveApp.getLeaveStatus().getName().contains("Approved"))
            {
            	log.info("Scheduled Task running to update Leave Status for: {}", leaveApp.toString());
            	try {
					LeaveApplication updatedLeaveApp = this.leaveAppService.updateStatus(leaveApp.getId(), "Taken");
					log.info("Scheduled Task updated Leave Status for: {}", updatedLeaveApp.toString());
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
            } else {
            	continue;
            }
        }
        log.info("[Scheduled Task Complete]");
    }
}
