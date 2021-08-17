package za.co.dearx.leave.service;

import camundajar.impl.scala.Option;
import java.math.BigDecimal;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.EntitlementValue;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;
import za.co.dearx.leave.repository.LeaveTypeRepository;
import za.co.dearx.leave.repository.StaffRepository;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;
import za.co.dearx.leave.service.dto.StaffDTO;
import za.co.dearx.leave.service.mapper.StaffMapper;

/**
 * Service Implementation for managing {@link Staff}.
 */
@Service
@Transactional
public class StaffService {

    private final Logger log = LoggerFactory.getLogger(StaffService.class);

    private final StaffRepository staffRepository;

    private final LeaveTypeRepository leaveTypeRepository;

    private final StaffMapper staffMapper;

    public StaffService(
        StaffRepository staffRepository,
        StaffMapper staffMapper,
        LeaveEntitlementRepository leaveEntitlementRepository,
        LeaveTypeRepository leaveTypeRepository
    ) {
        this.staffRepository = staffRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.staffMapper = staffMapper;
    }

    /**
     * Save a staff.
     *
     * @param staffDTO the entity to save.
     * @return the persisted entity.
     */
    public StaffDTO save(StaffDTO staffDTO) {
        log.debug("Request to save Staff : {}", staffDTO);
        Staff staff = staffMapper.toEntity(staffDTO);
        staff = staffRepository.save(staff);
        return staffMapper.toDto(staff);
    }

    /**
     * Partially update a staff.
     *
     * @param staffDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StaffDTO> partialUpdate(StaffDTO staffDTO) {
        log.debug("Request to partially update Staff : {}", staffDTO);

        return staffRepository
            .findById(staffDTO.getId())
            .map(
                existingStaff -> {
                    staffMapper.partialUpdate(existingStaff, staffDTO);
                    return existingStaff;
                }
            )
            .map(staffRepository::save)
            .map(staffMapper::toDto);
    }

    /**
     * Updates the staff leave entitlement value using the staffDTO
     *
     * @param staffDTO
     * @return
     */
    public Optional<StaffDTO> updateLeaveEntitlement(Staff staff, BigDecimal days) {
        log.debug("Add number of Leave Entitlement days: {} to Staff Member: {}", staff, days);
        StaffDTO staffDTO = null;

        staff.setAnnualLeaveEntitlement(days);

        return staffRepository
            .findById(staff.getId())
            .map(
                existingStaff -> {
                    staffMapper.partialUpdate(staff, staffDTO);
                    return existingStaff;
                }
            )
            .map(staffRepository::save)
            .map(staffMapper::toDto);
    }

    /**
     * Get all the staff.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StaffDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Staff");
        return staffRepository.findAll(pageable).map(staffMapper::toDto);
    }

    /**
     * Get all the staff with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<StaffDTO> findAllWithEagerRelationships(Pageable pageable) {
        return staffRepository.findAllWithEagerRelationships(pageable).map(staffMapper::toDto);
    }

    /**
     * Get one staff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StaffDTO> findOne(Long id) {
        log.debug("Request to get Staff : {}", id);
        return staffRepository.findOneWithEagerRelationships(id).map(staffMapper::toDto);
    }

    /**
     * Delete the staff by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Staff : {}", id);
        staffRepository.deleteById(id);
    }
}
