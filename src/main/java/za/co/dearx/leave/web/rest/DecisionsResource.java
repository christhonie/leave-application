package za.co.dearx.leave.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.co.dearx.leave.service.DecisionsQueryService;
import za.co.dearx.leave.service.DecisionsService;
import za.co.dearx.leave.service.dto.DecisionsCriteria;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.Decisions}.
 */
@RestController
@RequestMapping("/api")
public class DecisionsResource {
    private final Logger log = LoggerFactory.getLogger(DecisionsResource.class);

    private static final String ENTITY_NAME = "decisions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecisionsService decisionsService;

    private final DecisionsQueryService decisionsQueryService;

    public DecisionsResource(DecisionsService decisionsService, DecisionsQueryService decisionsQueryService) {
        this.decisionsService = decisionsService;
        this.decisionsQueryService = decisionsQueryService;
    }

    /**
     * {@code POST  /decisions} : Create a new decisions.
     *
     * @param decisionsDTO the decisionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decisionsDTO, or with status {@code 400 (Bad Request)} if the decisions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decisions")
    public ResponseEntity<DecisionsDTO> createDecisions(@Valid @RequestBody DecisionsDTO decisionsDTO) throws URISyntaxException {
        log.debug("REST request to save Decisions : {}", decisionsDTO);
        if (decisionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new decisions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DecisionsDTO result = decisionsService.save(decisionsDTO);
        return ResponseEntity
            .created(new URI("/api/decisions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decisions} : Updates an existing decisions.
     *
     * @param decisionsDTO the decisionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decisionsDTO,
     * or with status {@code 400 (Bad Request)} if the decisionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decisionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decisions")
    public ResponseEntity<DecisionsDTO> updateDecisions(@Valid @RequestBody DecisionsDTO decisionsDTO) throws URISyntaxException {
        log.debug("REST request to update Decisions : {}", decisionsDTO);
        if (decisionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DecisionsDTO result = decisionsService.save(decisionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decisionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /decisions} : get all the decisions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decisions in body.
     */
    @GetMapping("/decisions")
    public ResponseEntity<List<DecisionsDTO>> getAllDecisions(DecisionsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Decisions by criteria: {}", criteria);
        Page<DecisionsDTO> page = decisionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /decisions/count} : count all the decisions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/decisions/count")
    public ResponseEntity<Long> countDecisions(DecisionsCriteria criteria) {
        log.debug("REST request to count Decisions by criteria: {}", criteria);
        return ResponseEntity.ok().body(decisionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /decisions/:id} : get the "id" decisions.
     *
     * @param id the id of the decisionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decisionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decisions/{id}")
    public ResponseEntity<DecisionsDTO> getDecisions(@PathVariable Long id) {
        log.debug("REST request to get Decisions : {}", id);
        Optional<DecisionsDTO> decisionsDTO = decisionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decisionsDTO);
    }

    /**
     * {@code DELETE  /decisions/:id} : delete the "id" decisions.
     *
     * @param id the id of the decisionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decisions/{id}")
    public ResponseEntity<Void> deleteDecisions(@PathVariable Long id) {
        log.debug("REST request to delete Decisions : {}", id);
        decisionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
