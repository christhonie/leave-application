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
import za.co.dearx.leave.repository.LeaveEntitlementRepository;
import za.co.dearx.leave.service.LeaveEntitlementQueryService;
import za.co.dearx.leave.service.LeaveEntitlementService;
import za.co.dearx.leave.service.criteria.LeaveEntitlementCriteria;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.LeaveEntitlement}.
 */
@RestController
@RequestMapping("/api")
public class LeaveEntitlementResource {

    private final Logger log = LoggerFactory.getLogger(LeaveEntitlementResource.class);

    private static final String ENTITY_NAME = "leaveEntitlement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaveEntitlementService leaveEntitlementService;

    private final LeaveEntitlementRepository leaveEntitlementRepository;

    private final LeaveEntitlementQueryService leaveEntitlementQueryService;

    public LeaveEntitlementResource(
        LeaveEntitlementService leaveEntitlementService,
        LeaveEntitlementRepository leaveEntitlementRepository,
        LeaveEntitlementQueryService leaveEntitlementQueryService
    ) {
        this.leaveEntitlementService = leaveEntitlementService;
        this.leaveEntitlementRepository = leaveEntitlementRepository;
        this.leaveEntitlementQueryService = leaveEntitlementQueryService;
    }

    /**
     * {@code POST  /leave-entitlements} : Create a new leaveEntitlement.
     *
     * @param leaveEntitlementDTO the leaveEntitlementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaveEntitlementDTO, or with status {@code 400 (Bad Request)} if the leaveEntitlement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/leave-entitlements")
    public ResponseEntity<LeaveEntitlementDTO> createLeaveEntitlement(@Valid @RequestBody LeaveEntitlementDTO leaveEntitlementDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaveEntitlement : {}", leaveEntitlementDTO);
        if (leaveEntitlementDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveEntitlement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveEntitlementDTO result = leaveEntitlementService.save(leaveEntitlementDTO);
        return ResponseEntity
            .created(new URI("/api/leave-entitlements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-entitlements/:id} : Updates an existing leaveEntitlement.
     *
     * @param id the id of the leaveEntitlementDTO to save.
     * @param leaveEntitlementDTO the leaveEntitlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveEntitlementDTO,
     * or with status {@code 400 (Bad Request)} if the leaveEntitlementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveEntitlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-entitlements/{id}")
    public ResponseEntity<LeaveEntitlementDTO> updateLeaveEntitlement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaveEntitlementDTO leaveEntitlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaveEntitlement : {}, {}", id, leaveEntitlementDTO);
        if (leaveEntitlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveEntitlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveEntitlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaveEntitlementDTO result = leaveEntitlementService.save(leaveEntitlementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveEntitlementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /leave-entitlements/:id} : Partial updates given fields of an existing leaveEntitlement, field will ignore if it is null
     *
     * @param id the id of the leaveEntitlementDTO to save.
     * @param leaveEntitlementDTO the leaveEntitlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveEntitlementDTO,
     * or with status {@code 400 (Bad Request)} if the leaveEntitlementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaveEntitlementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaveEntitlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/leave-entitlements/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeaveEntitlementDTO> partialUpdateLeaveEntitlement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaveEntitlementDTO leaveEntitlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaveEntitlement partially : {}, {}", id, leaveEntitlementDTO);
        if (leaveEntitlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaveEntitlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaveEntitlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaveEntitlementDTO> result = leaveEntitlementService.partialUpdate(leaveEntitlementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveEntitlementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /leave-entitlements} : get all the leaveEntitlements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaveEntitlements in body.
     */
    @GetMapping("/leave-entitlements")
    public ResponseEntity<List<LeaveEntitlementDTO>> getAllLeaveEntitlements(LeaveEntitlementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaveEntitlements by criteria: {}", criteria);
        Page<LeaveEntitlementDTO> page = leaveEntitlementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /leave-entitlements/count} : count all the leaveEntitlements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/leave-entitlements/count")
    public ResponseEntity<Long> countLeaveEntitlements(LeaveEntitlementCriteria criteria) {
        log.debug("REST request to count LeaveEntitlements by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaveEntitlementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leave-entitlements/:id} : get the "id" leaveEntitlement.
     *
     * @param id the id of the leaveEntitlementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaveEntitlementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/leave-entitlements/{id}")
    public ResponseEntity<LeaveEntitlementDTO> getLeaveEntitlement(@PathVariable Long id) {
        log.debug("REST request to get LeaveEntitlement : {}", id);
        Optional<LeaveEntitlementDTO> leaveEntitlementDTO = leaveEntitlementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaveEntitlementDTO);
    }

    /**
     * {@code DELETE  /leave-entitlements/:id} : delete the "id" leaveEntitlement.
     *
     * @param id the id of the leaveEntitlementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/leave-entitlements/{id}")
    public ResponseEntity<Void> deleteLeaveEntitlement(@PathVariable Long id) {
        log.debug("REST request to delete LeaveEntitlement : {}", id);
        leaveEntitlementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
