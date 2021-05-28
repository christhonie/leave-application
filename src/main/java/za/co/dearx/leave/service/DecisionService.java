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
import za.co.dearx.leave.domain.Decision;
import za.co.dearx.leave.repository.DecisionRepository;
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.dto.DecisionDTO;
import za.co.dearx.leave.service.exception.ValidationException;
import za.co.dearx.leave.service.mapper.DecisionMapper;
import za.co.dearx.leave.service.mapper.LeaveApplicationMapper;

/**
 * Service Implementation for managing {@link Decision}.
 */
@Service
@Transactional
public class DecisionService {

    private final Logger log = LoggerFactory.getLogger(DecisionService.class);

    private final DecisionRepository decisionRepository;

    private final DecisionMapper decisionMapper;

    private final MessageHander messageHandler;

    private final LeaveApplicationService leaveApplicationService;

    public DecisionService(
        DecisionRepository decisionRepository,
        DecisionMapper decisionMapper,
        MessageHander messageHandler,
        LeaveApplicationService leaveApplicationService
    ) {
        this.decisionRepository = decisionRepository;
        this.decisionMapper = decisionMapper;
        this.messageHandler = messageHandler;
        this.leaveApplicationService = leaveApplicationService;
    }

    /**
     * Save a decision.
     *
     * @param decisionDTO the entity to save.
     * @return the persisted entity.
     */
    public DecisionDTO save(DecisionDTO decisionDTO) {
        log.debug("Request to save Decision : {}", decisionDTO);
        Decision decision = decisionMapper.toEntity(decisionDTO);

        //Load latest LeaveApplication from database
        if (decision.getLeaveApplication() != null) leaveApplicationService
            .findOneEntity(decision.getLeaveApplication().getId())
            .ifPresent(
                la -> {
                    decision.setLeaveApplication(la);
                }
            );

        //TODO Investigate if this was really necessary
        if (decisionDTO.getComment() != null && decision.getComment() == null) {
            Comment comment = new Comment();
            comment.setComment(decisionDTO.getComment().getComment());
            decision.setComment(comment);
        }
        Decision result = decisionRepository.save(decision);
        // Call messageHandler and pass in withdraw...
        try {
            messageHandler.processDecicion(result);
        } catch (ValidationException | NoMessageCatchException e) {
            // TODO Auto-generated catch block
            log.debug("Could not throw withdraw message ", e);
        }
        return decisionMapper.toDto(result);
    }

    /**
     * Partially update a decision.
     *
     * @param decisionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DecisionDTO> partialUpdate(DecisionDTO decisionDTO) {
        log.debug("Request to partially update Decision : {}", decisionDTO);

        return decisionRepository
            .findById(decisionDTO.getId())
            .map(
                existingDecision -> {
                    decisionMapper.partialUpdate(existingDecision, decisionDTO);
                    return existingDecision;
                }
            )
            .map(decisionRepository::save)
            .map(decisionMapper::toDto);
    }

    /**
     * Get all the decisions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Decisions");
        return decisionRepository.findAll(pageable).map(decisionMapper::toDto);
    }

    /**
     * Get one decision by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DecisionDTO> findOne(Long id) {
        log.debug("Request to get Decision : {}", id);
        return decisionRepository.findById(id).map(decisionMapper::toDto);
    }

    /**
     * Delete the decision by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Decision : {}", id);
        decisionRepository.deleteById(id);
    }
}
