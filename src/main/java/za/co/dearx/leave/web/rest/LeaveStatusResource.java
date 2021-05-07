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
import za.co.dearx.leave.repository.LeaveStatusRepository;
import za.co.dearx.leave.service.LeaveStatusQueryService;
import za.co.dearx.leave.service.LeaveStatusService;
import za.co.dearx.leave.service.criteria.LeaveStatusCriteria;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.LeaveStatus}.
 */
@RestController
@RequestMapping("/api")
public class LeaveStatusResource {

    private final Logger log = LoggerFactory.getLogger(LeaveStatusResource.class);

    private static final String ENTITY_NAME = "leaveStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveStatusService leaveStatusService;

    private final LeaveStatusRepository leaveStatusRepository;

    private final LeaveStatusQueryService leaveStatusQueryService;

    public LeaveStatusResource(
        LeaveStatusService leaveStatusService,
        LeaveStatusRepository leaveStatusRepository,
        LeaveStatusQueryService leaveStatusQueryService
    ) {
        this.leaveStatusService = leaveStatusService;
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveStatusQueryService = leaveStatusQueryService;
    }

    /**
     * {@code POST  /leave-statuses} : Create a new leaveStatus.
     *
     * @param leaveStatusDTO the leaveStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveStatusDTO, or with status {@code 400 (Bad Request)} if the leaveStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-statuses")
    public ResponseEntity<LeaveStatusDTO> createLeaveStatus(@Valid @RequestBody LeaveStatusDTO leaveStatusDTO) throws URISyntaxException {
        log.debug("REST request to save LeaveStatus : {}", leaveStatusDTO);
        if (leaveStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveStatusDTO result = leaveStatusService.save(leaveStatusDTO);
        return ResponseEntity
            .created(new URI("/api/leave-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-statuses/:id} : Updates an existing leaveStatus.
     *
     * @param id the id of the leaveStatusDTO to save.
     * @param leaveStatusDTO the leaveStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveStatusDTO,
     * or with status {@code 400 (Bad Request)} if the leaveStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-statuses/{id}")
    public ResponseEntity<LeaveStatusDTO> updateLeaveStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaveStatusDTO leaveStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveStatus : {}, {}", id, leaveStatusDTO);
        if (leaveStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaveStatusDTO result = leaveStatusService.save(leaveStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leave-statuses/:id} : Partial updates given fields of an existing leaveStatus, field will ignore if it is null
     *
     * @param id the id of the leaveStatusDTO to save.
     * @param leaveStatusDTO the leaveStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveStatusDTO,
     * or with status {@code 400 (Bad Request)} if the leaveStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaveStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leave-statuses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeaveStatusDTO> partialUpdateLeaveStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaveStatusDTO leaveStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveStatus partially : {}, {}", id, leaveStatusDTO);
        if (leaveStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaveStatusDTO> result = leaveStatusService.partialUpdate(leaveStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /leave-statuses} : get all the leaveStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveStatuses in body.
     */
    @GetMapping("/leave-statuses")
    public ResponseEntity<List<LeaveStatusDTO>> getAllLeaveStatuses(LeaveStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveStatuses by criteria: {}", criteria);
        Page<LeaveStatusDTO> page = leaveStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-statuses/count} : count all the leaveStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leave-statuses/count")
    public ResponseEntity<Long> countLeaveStatuses(LeaveStatusCriteria criteria) {
        log.debug("REST request to count LeaveStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaveStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leave-statuses/:id} : get the "id" leaveStatus.
     *
     * @param id the id of the leaveStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-statuses/{id}")
    public ResponseEntity<LeaveStatusDTO> getLeaveStatus(@PathVariable Long id) {
        log.debug("REST request to get LeaveStatus : {}", id);
        Optional<LeaveStatusDTO> leaveStatusDTO = leaveStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveStatusDTO);
    }

    /**
     * {@code DELETE  /leave-statuses/:id} : delete the "id" leaveStatus.
     *
     * @param id the id of the leaveStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leave-statuses/{id}")
    public ResponseEntity<Void> deleteLeaveStatus(@PathVariable Long id) {
        log.debug("REST request to delete LeaveStatus : {}", id);
        leaveStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
