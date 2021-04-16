package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.repository.LeaveStatusRepository;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;
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

    public LeaveStatusService(LeaveStatusRepository leaveStatusRepository, LeaveStatusMapper leaveStatusMapper) {
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveStatusMapper = leaveStatusMapper;
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

    public static LeaveStatus getDraft() {
        // TODO Find a way to return the default Draft state
        return null;
    }
}
