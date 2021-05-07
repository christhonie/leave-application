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
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.LeaveApplicationQueryService;
import za.co.dearx.leave.service.LeaveApplicationService;
import za.co.dearx.leave.service.criteria.LeaveApplicationCriteria;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.exception.ValidationException;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.LeaveApplication}.
 */
@RestController
@RequestMapping("/api")
public class LeaveApplicationResource {

    private final Logger log = LoggerFactory.getLogger(LeaveApplicationResource.class);

    private static final String ENTITY_NAME = "leaveApplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveApplicationService leaveApplicationService;

    private final LeaveApplicationRepository leaveApplicationRepository;

    private final LeaveApplicationQueryService leaveApplicationQueryService;

    public LeaveApplicationResource(
        LeaveApplicationService leaveApplicationService,
        LeaveApplicationRepository leaveApplicationRepository,
        LeaveApplicationQueryService leaveApplicationQueryService
    ) {
        this.leaveApplicationService = leaveApplicationService;
        this.leaveApplicationRepository = leaveApplicationRepository;
        this.leaveApplicationQueryService = leaveApplicationQueryService;
    }

    /**
     * {@code POST  /leave-applications} : Create a new leaveApplication.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveApplicationDTO, or with status {@code 400 (Bad Request)} if the leaveApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-applications")
    public ResponseEntity<LeaveApplicationDTO> createLeaveApplication(@Valid @RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to create LeaveApplication for normal CRUD operations: {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveApplicationDTO result;
        try {
            result = leaveApplicationService.create(leaveApplicationDTO);
            return ResponseEntity
                .created(new URI("/api/leave-applications/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }
    }

    /**
     * {@code POST  /leave-applications/save} : Create/Save a new leaveApplicatio before submission as part of the application process.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveApplicationDTO, or with status {@code 400 (Bad Request)} if the leaveApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-applications/save")
    public ResponseEntity<LeaveApplicationDTO> saveLeaveApplication(@Valid @RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveApplication during leave process: {}", leaveApplicationDTO);
        LeaveApplicationDTO result;
        try {
            result = leaveApplicationService.save(leaveApplicationDTO);
        } catch (ValidationException e) {
            throw new BadRequestAlertException("Invalid data", ENTITY_NAME, "validation");
        }
        return ResponseEntity
            .created(new URI("/api/leave-applications/save" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /leave-applications/submit} : Submit a new or saved leaveApplicatio as part of the application process.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveApplicationDTO, or with status {@code 400 (Bad Request)} if the leaveApplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-applications/submit")
    public ResponseEntity<LeaveApplicationDTO> submitLeaveApplication(@Valid @RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to submit LeaveApplication during leave process: {}", leaveApplicationDTO);
        //TODO Either set created or updated response
        LeaveApplicationDTO result;
        try {
            result = leaveApplicationService.save(leaveApplicationDTO);
        } catch (ValidationException e) {
            throw new BadRequestAlertException("Invalid data", ENTITY_NAME, "validation");
        }
        return ResponseEntity
            .created(new URI("/api/leave-applications/submit" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-applications} : Updates an existing leaveApplication.
     *
     * @param id the id of the leaveApplicationDTO to save.
     * @param leaveApplicationDTO the leaveApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the leaveApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-applications/{id}")
    public ResponseEntity<LeaveApplicationDTO> updateLeaveApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaveApplicationDTO leaveApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveApplication : {}, {}", id, leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaveApplicationDTO result;
        try {
            result = leaveApplicationService.save(leaveApplicationDTO);
        } catch (ValidationException e) {
            throw new BadRequestAlertException("Invalid data", ENTITY_NAME, "validation");
        }
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveApplicationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leave-applications/:id} : Partial updates given fields of an existing leaveApplication, field will ignore if it is null
     *
     * @param id the id of the leaveApplicationDTO to save.
     * @param leaveApplicationDTO the leaveApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the leaveApplicationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaveApplicationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leave-applications/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeaveApplicationDTO> partialUpdateLeaveApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaveApplicationDTO leaveApplicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveApplication partially : {}, {}", id, leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveApplicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveApplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaveApplicationDTO> result = leaveApplicationService.partialUpdate(leaveApplicationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveApplicationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /leave-applications} : get all the leaveApplications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveApplications in body.
     */
    @GetMapping("/leave-applications")
    public ResponseEntity<List<LeaveApplicationDTO>> getAllLeaveApplications(LeaveApplicationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveApplications by criteria: {}", criteria);
        Page<LeaveApplicationDTO> page = leaveApplicationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-applications/count} : count all the leaveApplications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leave-applications/count")
    public ResponseEntity<Long> countLeaveApplications(LeaveApplicationCriteria criteria) {
        log.debug("REST request to count LeaveApplications by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaveApplicationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leave-applications/:id} : get the "id" leaveApplication.
     *
     * @param id the id of the leaveApplicationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveApplicationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-applications/{id}")
    public ResponseEntity<LeaveApplicationDTO> getLeaveApplication(@PathVariable Long id) {
        log.debug("REST request to get LeaveApplication : {}", id);
        Optional<LeaveApplicationDTO> leaveApplicationDTO = leaveApplicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveApplicationDTO);
    }

    /**
     * {@code DELETE  /leave-applications/:id} : delete the "id" leaveApplication.
     *
     * @param id the id of the leaveApplicationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leave-applications/{id}")
    public ResponseEntity<Void> deleteLeaveApplication(@PathVariable Long id) {
        log.debug("REST request to delete LeaveApplication : {}", id);
        leaveApplicationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
