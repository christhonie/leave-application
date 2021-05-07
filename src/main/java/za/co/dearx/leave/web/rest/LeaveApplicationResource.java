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
import za.co.dearx.leave.service.LeaveApplicationQueryService;
import za.co.dearx.leave.service.LeaveApplicationService;
import za.co.dearx.leave.service.dto.LeaveApplicationCriteria;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
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

    private final LeaveApplicationQueryService leaveApplicationQueryService;

    public LeaveApplicationResource(
        LeaveApplicationService leaveApplicationService,
        LeaveApplicationQueryService leaveApplicationQueryService
    ) {
        this.leaveApplicationService = leaveApplicationService;
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
        log.debug("REST request to save LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaveApplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO);
        return ResponseEntity
            .created(new URI("/api/leave-applications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /leave-applications} : Updates an existing leaveApplication.
     *
     * @param leaveApplicationDTO the leaveApplicationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaveApplicationDTO,
     * or with status {@code 400 (Bad Request)} if the leaveApplicationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaveApplicationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/leave-applications")
    public ResponseEntity<LeaveApplicationDTO> updateLeaveApplication(@Valid @RequestBody LeaveApplicationDTO leaveApplicationDTO)
        throws URISyntaxException {
        log.debug("REST request to update LeaveApplication : {}", leaveApplicationDTO);
        if (leaveApplicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeaveApplicationDTO result = leaveApplicationService.save(leaveApplicationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaveApplicationDTO.getId().toString()))
            .body(result);
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
