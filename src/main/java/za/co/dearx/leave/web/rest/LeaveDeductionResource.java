package za.co.dearx.leave.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import za.co.dearx.leave.repository.LeaveDeductionRepository;
import za.co.dearx.leave.service.LeaveDeductionQueryService;
import za.co.dearx.leave.service.LeaveDeductionService;
import za.co.dearx.leave.service.criteria.LeaveDeductionCriteria;
import za.co.dearx.leave.service.dto.LeaveDeductionDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.LeaveDeduction}.
 */
@RestController
@RequestMapping("/api")
public class LeaveDeductionResource {

    private final Logger log = LoggerFactory.getLogger(LeaveDeductionResource.class);

    private static final String ENTITY_NAME = "leaveDeduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveDeductionService leaveDeductionService;

    private final LeaveDeductionRepository leaveDeductionRepository;

    private final LeaveDeductionQueryService leaveDeductionQueryService;

    public LeaveDeductionResource(
        LeaveDeductionService leaveDeductionService,
        LeaveDeductionRepository leaveDeductionRepository,
        LeaveDeductionQueryService leaveDeductionQueryService
    ) {
        this.leaveDeductionService = leaveDeductionService;
        this.leaveDeductionRepository = leaveDeductionRepository;
        this.leaveDeductionQueryService = leaveDeductionQueryService;
    }

    /**
     * {@code POST  /leave-deductions} : Create a new leaveDeduction.
     *
     * @param leaveDeductionDTO the leaveDeductionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveDeductionDTO, or with status {@code 400 (Bad Request)} if the leaveDeduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-deductions")
    public ResponseEntity<LeaveDeductionDTO> createLeaveDeduction(@Valid @RequestBody LeaveDeductionDTO leaveDeductionDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveDeduction : {}", leaveDeductionDTO);
        if (leaveDeductionDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveDeduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveDeductionDTO result = leaveDeductionService.save(leaveDeductionDTO);
        return ResponseEntity
            .created(new URI("/api/leave-deductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-deductions/:id} : Updates an existing leaveDeduction.
     *
     * @param id the id of the leaveDeductionDTO to save.
     * @param leaveDeductionDTO the leaveDeductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveDeductionDTO,
     * or with status {@code 400 (Bad Request)} if the leaveDeductionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveDeductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-deductions/{id}")
    public ResponseEntity<LeaveDeductionDTO> updateLeaveDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaveDeductionDTO leaveDeductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveDeduction : {}, {}", id, leaveDeductionDTO);
        if (leaveDeductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveDeductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveDeductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaveDeductionDTO result = leaveDeductionService.save(leaveDeductionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveDeductionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leave-deductions/:id} : Partial updates given fields of an existing leaveDeduction, field will ignore if it is null
     *
     * @param id the id of the leaveDeductionDTO to save.
     * @param leaveDeductionDTO the leaveDeductionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveDeductionDTO,
     * or with status {@code 400 (Bad Request)} if the leaveDeductionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaveDeductionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveDeductionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leave-deductions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeaveDeductionDTO> partialUpdateLeaveDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaveDeductionDTO leaveDeductionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveDeduction partially : {}, {}", id, leaveDeductionDTO);
        if (leaveDeductionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveDeductionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveDeductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaveDeductionDTO> result = leaveDeductionService.partialUpdate(leaveDeductionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveDeductionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /leave-deductions} : get all the leaveDeductions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveDeductions in body.
     */
    @GetMapping("/leave-deductions")
    public ResponseEntity<List<LeaveDeductionDTO>> getAllLeaveDeductions(LeaveDeductionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveDeductions by criteria: {}", criteria);
        Page<LeaveDeductionDTO> page = leaveDeductionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-deductions/count} : count all the leaveDeductions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leave-deductions/count")
    public ResponseEntity<Long> countLeaveDeductions(LeaveDeductionCriteria criteria) {
        log.debug("REST request to count LeaveDeductions by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaveDeductionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leave-deductions/:id} : get the "id" leaveDeduction.
     *
     * @param id the id of the leaveDeductionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveDeductionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-deductions/{id}")
    public ResponseEntity<LeaveDeductionDTO> getLeaveDeduction(@PathVariable Long id) {
        log.debug("REST request to get LeaveDeduction : {}", id);
        Optional<LeaveDeductionDTO> leaveDeductionDTO = leaveDeductionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveDeductionDTO);
    }

    /**
     * {@code DELETE  /leave-deductions/:id} : delete the "id" leaveDeduction.
     *
     * @param id the id of the leaveDeductionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leave-deductions/{id}")
    public ResponseEntity<Void> deleteLeaveDeduction(@PathVariable Long id) {
        log.debug("REST request to delete LeaveDeduction : {}", id);
        leaveDeductionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
