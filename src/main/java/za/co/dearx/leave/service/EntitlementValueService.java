package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.EntitlementValue;
import za.co.dearx.leave.repository.EntitlementValueRepository;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;
import za.co.dearx.leave.service.mapper.EntitlementValueMapper;

/**
 * Service Implementation for managing {@link EntitlementValue}.
 */
@Service
@Transactional
public class EntitlementValueService {
    private final Logger log = LoggerFactory.getLogger(EntitlementValueService.class);

    private final EntitlementValueRepository entitlementValueRepository;

    private final EntitlementValueMapper entitlementValueMapper;

    public EntitlementValueService(EntitlementValueRepository entitlementValueRepository, EntitlementValueMapper entitlementValueMapper) {
        this.entitlementValueRepository = entitlementValueRepository;
        this.entitlementValueMapper = entitlementValueMapper;
    }

    /**
     * Save a entitlementValue.
     *
     * @param entitlementValueDTO the entity to save.
     * @return the persisted entity.
     */
    public EntitlementValueDTO save(EntitlementValueDTO entitlementValueDTO) {
        log.debug("Request to save EntitlementValue : {}", entitlementValueDTO);
        EntitlementValue entitlementValue = entitlementValueMapper.toEntity(entitlementValueDTO);
        entitlementValue = entitlementValueRepository.save(entitlementValue);
        return entitlementValueMapper.toDto(entitlementValue);
    }

    /**
     * Get all the entitlementValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntitlementValueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EntitlementValues");
        return entitlementValueRepository.findAll(pageable).map(entitlementValueMapper::toDto);
    }

    /**
     * Get one entitlementValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntitlementValueDTO> findOne(Long id) {
        log.debug("Request to get EntitlementValue : {}", id);
        return entitlementValueRepository.findById(id).map(entitlementValueMapper::toDto);
    }

    /**
     * Delete the entitlementValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EntitlementValue : {}", id);
        entitlementValueRepository.deleteById(id);
    }
}
