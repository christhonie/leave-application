package za.co.dearx.leave.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.bpmn.MessageHander;
import za.co.dearx.leave.bpmn.exception.NoMessageCatchException;
import za.co.dearx.leave.domain.Comment;
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.repository.DecisionsRepository;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.service.exception.ValidationException;
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

    private final MessageHander messageHandler;

    private final LeaveApplicationService leaveApplicationService;

    public DecisionsService(
        DecisionsRepository decisionsRepository,
        DecisionsMapper decisionsMapper,
        MessageHander messageHandler,
        LeaveApplicationService leaveApplicationService
    ) {
        this.decisionsRepository = decisionsRepository;
        this.decisionsMapper = decisionsMapper;
        this.messageHandler = messageHandler;
        this.leaveApplicationService = leaveApplicationService;
    }

    /**
     * Save a decisions.
     *
     * @param decisionsDTO the entity to save.
     * @return the persisted entity.
     */
    public DecisionsDTO save(DecisionsDTO decisionsDTO) {
        log.debug("Request to save Decisions : {}", decisionsDTO);
        final Decisions decisions = decisionsMapper.toEntity(decisionsDTO);

        //Load latest LeaveApplication from database
        if (decisions.getLeaveApplication() != null) leaveApplicationService
            .findOneEntity(decisions.getLeaveApplication().getId())
            .ifPresent(
                la -> {
                    decisions.setLeaveApplication(la);
                }
            );

        //TODO Investigate if this was really necessary
        if (decisionsDTO.getComment() != null && decisions.getComment() == null) {
            Comment comment = new Comment();
            comment.setComment(decisionsDTO.getComment().getComment());
            decisions.setComment(comment);
        }
        Decisions result = decisionsRepository.save(decisions);
        // Call messageHandler and pass in withdraw...
        try {
            messageHandler.processDecicion(result);
        } catch (ValidationException | NoMessageCatchException e) {
            // TODO Auto-generated catch block
            log.debug("Could not throw withdraw message ", e);
        }
        return decisionsMapper.toDto(result);
    }

    /**
     * Partially update a decisions.
     *
     * @param decisionsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DecisionsDTO> partialUpdate(DecisionsDTO decisionsDTO) {
        log.debug("Request to partially update Decisions : {}", decisionsDTO);

        return decisionsRepository
            .findById(decisionsDTO.getId())
            .map(
                existingDecisions -> {
                    decisionsMapper.partialUpdate(existingDecisions, decisionsDTO);
                    return existingDecisions;
                }
            )
            .map(decisionsRepository::save)
            .map(decisionsMapper::toDto);
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
