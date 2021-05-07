package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.repository.DecisionsRepository;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.service.mapper.DecisionsMapper;

/**
 * Service Implementation for managing {@link Decisions}.
 */
@Service
@Transactional
public class DecisionsService {
    private final Logger log = LoggerFactory.getLogger(DecisionsService.class);

    private final DecisionsRepository decisionsRepository;

    private final DecisionsMapper decisionsMapper;

    public DecisionsService(DecisionsRepository decisionsRepository, DecisionsMapper decisionsMapper) {
        this.decisionsRepository = decisionsRepository;
        this.decisionsMapper = decisionsMapper;
    }

    /**
     * Save a decisions.
     *
     * @param decisionsDTO the entity to save.
     * @return the persisted entity.
     */
    public DecisionsDTO save(DecisionsDTO decisionsDTO) {
        log.debug("Request to save Decisions : {}", decisionsDTO);
        Decisions decisions = decisionsMapper.toEntity(decisionsDTO);
        //TODO Investigate if this was really necessary
        decisions.getComment().setComment(decisionsDTO.getCommentComment());
        decisions = decisionsRepository.save(decisions);
        return decisionsMapper.toDto(decisions);
    }

    /**
     * Get all the decisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Decisions");
        return decisionsRepository.findAll(pageable).map(decisionsMapper::toDto);
    }

    /**
     * Get one decisions by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DecisionsDTO> findOne(Long id) {
        log.debug("Request to get Decisions : {}", id);
        return decisionsRepository.findById(id).map(decisionsMapper::toDto);
    }

    /**
     * Delete the decisions by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Decisions : {}", id);
        decisionsRepository.deleteById(id);
    }
}
