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
import za.co.dearx.leave.service.EntitlementValueQueryService;
import za.co.dearx.leave.service.EntitlementValueService;
import za.co.dearx.leave.service.dto.EntitlementValueCriteria;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.EntitlementValue}.
 */
@RestController
@RequestMapping("/api")
public class EntitlementValueResource {
    private final Logger log = LoggerFactory.getLogger(EntitlementValueResource.class);

    private static final String ENTITY_NAME = "entitlementValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntitlementValueService entitlementValueService;

    private final EntitlementValueQueryService entitlementValueQueryService;

    public EntitlementValueResource(
        EntitlementValueService entitlementValueService,
        EntitlementValueQueryService entitlementValueQueryService
    ) {
        this.entitlementValueService = entitlementValueService;
        this.entitlementValueQueryService = entitlementValueQueryService;
    }

    /**
     * {@code POST  /entitlement-values} : Create a new entitlementValue.
     *
     * @param entitlementValueDTO the entitlementValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entitlementValueDTO, or with status {@code 400 (Bad Request)} if the entitlementValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entitlement-values")
    public ResponseEntity<EntitlementValueDTO> createEntitlementValue(@Valid @RequestBody EntitlementValueDTO entitlementValueDTO)
        throws URISyntaxException {
        log.debug("REST request to save EntitlementValue : {}", entitlementValueDTO);
        if (entitlementValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new entitlementValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntitlementValueDTO result = entitlementValueService.save(entitlementValueDTO);
        return ResponseEntity
            .created(new URI("/api/entitlement-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entitlement-values} : Updates an existing entitlementValue.
     *
     * @param entitlementValueDTO the entitlementValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entitlementValueDTO,
     * or with status {@code 400 (Bad Request)} if the entitlementValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entitlementValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entitlement-values")
    public ResponseEntity<EntitlementValueDTO> updateEntitlementValue(@Valid @RequestBody EntitlementValueDTO entitlementValueDTO)
        throws URISyntaxException {
        log.debug("REST request to update EntitlementValue : {}", entitlementValueDTO);
        if (entitlementValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntitlementValueDTO result = entitlementValueService.save(entitlementValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entitlementValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entitlement-values} : get all the entitlementValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entitlementValues in body.
     */
    @GetMapping("/entitlement-values")
    public ResponseEntity<List<EntitlementValueDTO>> getAllEntitlementValues(EntitlementValueCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EntitlementValues by criteria: {}", criteria);
        Page<EntitlementValueDTO> page = entitlementValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /entitlement-values/count} : count all the entitlementValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/entitlement-values/count")
    public ResponseEntity<Long> countEntitlementValues(EntitlementValueCriteria criteria) {
        log.debug("REST request to count EntitlementValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(entitlementValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /entitlement-values/:id} : get the "id" entitlementValue.
     *
     * @param id the id of the entitlementValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entitlementValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entitlement-values/{id}")
    public ResponseEntity<EntitlementValueDTO> getEntitlementValue(@PathVariable Long id) {
        log.debug("REST request to get EntitlementValue : {}", id);
        Optional<EntitlementValueDTO> entitlementValueDTO = entitlementValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entitlementValueDTO);
    }

    /**
     * {@code DELETE  /entitlement-values/:id} : delete the "id" entitlementValue.
     *
     * @param id the id of the entitlementValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entitlement-values/{id}")
    public ResponseEntity<Void> deleteEntitlementValue(@PathVariable Long id) {
        log.debug("REST request to delete EntitlementValue : {}", id);
        entitlementValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
