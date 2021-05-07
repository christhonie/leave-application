package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.repository.LeaveTypeRepository;
import za.co.dearx.leave.service.dto.LeaveTypeDTO;
import za.co.dearx.leave.service.mapper.LeaveTypeMapper;

/**
 * Service Implementation for managing {@link LeaveType}.
 */
@Service
@Transactional
public class LeaveTypeService {

    private final Logger log = LoggerFactory.getLogger(LeaveTypeService.class);

    private final LeaveTypeRepository leaveTypeRepository;

    private final LeaveTypeMapper leaveTypeMapper;

    public LeaveTypeService(LeaveTypeRepository leaveTypeRepository, LeaveTypeMapper leaveTypeMapper) {
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveTypeMapper = leaveTypeMapper;
    }

    /**
     * Save a leaveType.
     *
     * @param leaveTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public LeaveTypeDTO save(LeaveTypeDTO leaveTypeDTO) {
        log.debug("Request to save LeaveType : {}", leaveTypeDTO);
        LeaveType leaveType = leaveTypeMapper.toEntity(leaveTypeDTO);
        leaveType = leaveTypeRepository.save(leaveType);
        return leaveTypeMapper.toDto(leaveType);
    }

    /**
     * Partially update a leaveType.
     *
     * @param leaveTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeaveTypeDTO> partialUpdate(LeaveTypeDTO leaveTypeDTO) {
        log.debug("Request to partially update LeaveType : {}", leaveTypeDTO);

        return leaveTypeRepository
            .findById(leaveTypeDTO.getId())
            .map(
                existingLeaveType -> {
                    leaveTypeMapper.partialUpdate(existingLeaveType, leaveTypeDTO);
                    return existingLeaveType;
                }
            )
            .map(leaveTypeRepository::save)
            .map(leaveTypeMapper::toDto);
    }

    /**
     * Get all the leaveTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaveTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeaveTypes");
        return leaveTypeRepository.findAll(pageable).map(leaveTypeMapper::toDto);
    }

    /**
     * Get one leaveType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveTypeDTO> findOne(Long id) {
        log.debug("Request to get LeaveType : {}", id);
        return leaveTypeRepository.findById(id).map(leaveTypeMapper::toDto);
    }

    /**
     * Delete the leaveType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LeaveType : {}", id);
        leaveTypeRepository.deleteById(id);
    }

    /**
     * Get one leaveType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeaveType> findEntityById(Long id) {
        log.debug("Request to get LeaveType entity by ID: {}", id);
        return leaveTypeRepository.findById(id);
    }
}
