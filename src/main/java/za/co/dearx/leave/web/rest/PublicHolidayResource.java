package za.co.dearx.leave.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import za.co.dearx.leave.repository.PublicHolidayRepository;
import za.co.dearx.leave.service.PublicHolidayQueryService;
import za.co.dearx.leave.service.PublicHolidayService;
import za.co.dearx.leave.service.criteria.PublicHolidayCriteria;
import za.co.dearx.leave.service.dto.PublicHolidayDTO;
import za.co.dearx.leave.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link za.co.dearx.leave.domain.PublicHoliday}.
 */
@RestController
@RequestMapping("/api")
public class PublicHolidayResource {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayResource.class);

    private static final String ENTITY_NAME = "publicHoliday";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublicHolidayService publicHolidayService;

    private final PublicHolidayRepository publicHolidayRepository;

    private final PublicHolidayQueryService publicHolidayQueryService;

    public PublicHolidayResource(
        PublicHolidayService publicHolidayService,
        PublicHolidayRepository publicHolidayRepository,
        PublicHolidayQueryService publicHolidayQueryService
    ) {
        this.publicHolidayService = publicHolidayService;
        this.publicHolidayRepository = publicHolidayRepository;
        this.publicHolidayQueryService = publicHolidayQueryService;
    }

    /**
     * {@code POST  /public-holidays} : Create a new publicHoliday.
     *
     * @param publicHolidayDTO the publicHolidayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publicHolidayDTO, or with status {@code 400 (Bad Request)} if the publicHoliday has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/public-holidays")
    public ResponseEntity<PublicHolidayDTO> createPublicHoliday(@RequestBody PublicHolidayDTO publicHolidayDTO) throws URISyntaxException {
        log.debug("REST request to save PublicHoliday : {}", publicHolidayDTO);
        if (publicHolidayDTO.getId() != null) {
            throw new BadRequestAlertException("A new publicHoliday cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublicHolidayDTO result = publicHolidayService.save(publicHolidayDTO);
        return ResponseEntity
            .created(new URI("/api/public-holidays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /public-holidays/:id} : Updates an existing publicHoliday.
     *
     * @param id the id of the publicHolidayDTO to save.
     * @param publicHolidayDTO the publicHolidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publicHolidayDTO,
     * or with status {@code 400 (Bad Request)} if the publicHolidayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publicHolidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/public-holidays/{id}")
    public ResponseEntity<PublicHolidayDTO> updatePublicHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublicHolidayDTO publicHolidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PublicHoliday : {}, {}", id, publicHolidayDTO);
        if (publicHolidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publicHolidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publicHolidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PublicHolidayDTO result = publicHolidayService.save(publicHolidayDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publicHolidayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /public-holidays/:id} : Partial updates given fields of an existing publicHoliday, field will ignore if it is null
     *
     * @param id the id of the publicHolidayDTO to save.
     * @param publicHolidayDTO the publicHolidayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publicHolidayDTO,
     * or with status {@code 400 (Bad Request)} if the publicHolidayDTO is not valid,
     * or with status {@code 404 (Not Found)} if the publicHolidayDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the publicHolidayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/public-holidays/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PublicHolidayDTO> partialUpdatePublicHoliday(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublicHolidayDTO publicHolidayDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PublicHoliday partially : {}, {}", id, publicHolidayDTO);
        if (publicHolidayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publicHolidayDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publicHolidayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PublicHolidayDTO> result = publicHolidayService.partialUpdate(publicHolidayDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publicHolidayDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /public-holidays} : get all the publicHolidays.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publicHolidays in body.
     */
    @GetMapping("/public-holidays")
    public ResponseEntity<List<PublicHolidayDTO>> getAllPublicHolidays(PublicHolidayCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PublicHolidays by criteria: {}", criteria);
        Page<PublicHolidayDTO> page = publicHolidayQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /public-holidays/count} : count all the publicHolidays.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/public-holidays/count")
    public ResponseEntity<Long> countPublicHolidays(PublicHolidayCriteria criteria) {
        log.debug("REST request to count PublicHolidays by criteria: {}", criteria);
        return ResponseEntity.ok().body(publicHolidayQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /public-holidays/:id} : get the "id" publicHoliday.
     *
     * @param id the id of the publicHolidayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publicHolidayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/public-holidays/{id}")
    public ResponseEntity<PublicHolidayDTO> getPublicHoliday(@PathVariable Long id) {
        log.debug("REST request to get PublicHoliday : {}", id);
        Optional<PublicHolidayDTO> publicHolidayDTO = publicHolidayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publicHolidayDTO);
    }

    /**
     * {@code DELETE  /public-holidays/:id} : delete the "id" publicHoliday.
     *
     * @param id the id of the publicHolidayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/public-holidays/{id}")
    public ResponseEntity<Void> deletePublicHoliday(@PathVariable Long id) {
        log.debug("REST request to delete PublicHoliday : {}", id);
        publicHolidayService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/public-holidays/reload")
    public ResponseEntity<Void> reloadPublicHolidays() {
        log.debug("REST request to reload PublicHolidays");
        try {
            publicHolidayService.reloadSurrounding5Years();
        } catch (RestClientException | URISyntaxException e) {
            log.error("Could not reload public holiday data from Calendarrific API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public-holidays/working-days")
    public ResponseEntity<Integer> calculateWorkingDays(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        log.debug("REST request to reload PublicHolidays");
        try {
            int workingDays = publicHolidayService.calculateWorkingDays(startDate, endDate);
            System.out.println(workingDays);
        } catch (RestClientException e) {
            log.error("Could not reload public holiday data from Calendarrific API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        // TODO: Ensure correct response is returned
        return ResponseEntity.noContent().build();
    }
}
