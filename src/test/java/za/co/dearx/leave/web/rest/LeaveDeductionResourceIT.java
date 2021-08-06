package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.dearx.leave.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.IntegrationTest;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.LeaveDeduction;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.repository.LeaveDeductionRepository;
import za.co.dearx.leave.service.criteria.LeaveDeductionCriteria;
import za.co.dearx.leave.service.dto.LeaveDeductionDTO;
import za.co.dearx.leave.service.mapper.LeaveDeductionMapper;

/**
 * Integration tests for the {@link LeaveDeductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeaveDeductionResourceIT {

    private static final BigDecimal DEFAULT_DAYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DAYS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DAYS = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/leave-deductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LeaveDeductionRepository leaveDeductionRepository;

    @Autowired
    private LeaveDeductionMapper leaveDeductionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveDeductionMockMvc;

    private LeaveDeduction leaveDeduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveDeduction createEntity(EntityManager em) {
        LeaveDeduction leaveDeduction = new LeaveDeduction().days(DEFAULT_DAYS);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        leaveDeduction.setApplication(leaveApplication);
        // Add required entity
        LeaveEntitlement leaveEntitlement;
        if (TestUtil.findAll(em, LeaveEntitlement.class).isEmpty()) {
            leaveEntitlement = LeaveEntitlementResourceIT.createEntity(em);
            em.persist(leaveEntitlement);
            em.flush();
        } else {
            leaveEntitlement = TestUtil.findAll(em, LeaveEntitlement.class).get(0);
        }
        leaveDeduction.setEntitlement(leaveEntitlement);
        return leaveDeduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveDeduction createUpdatedEntity(EntityManager em) {
        LeaveDeduction leaveDeduction = new LeaveDeduction().days(UPDATED_DAYS);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createUpdatedEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        leaveDeduction.setApplication(leaveApplication);
        // Add required entity
        LeaveEntitlement leaveEntitlement;
        if (TestUtil.findAll(em, LeaveEntitlement.class).isEmpty()) {
            leaveEntitlement = LeaveEntitlementResourceIT.createUpdatedEntity(em);
            em.persist(leaveEntitlement);
            em.flush();
        } else {
            leaveEntitlement = TestUtil.findAll(em, LeaveEntitlement.class).get(0);
        }
        leaveDeduction.setEntitlement(leaveEntitlement);
        return leaveDeduction;
    }

    @BeforeEach
    public void initTest() {
        leaveDeduction = createEntity(em);
    }

    @Test
    @Transactional
    void createLeaveDeduction() throws Exception {
        int databaseSizeBeforeCreate = leaveDeductionRepository.findAll().size();
        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);
        restLeaveDeductionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveDeduction testLeaveDeduction = leaveDeductionList.get(leaveDeductionList.size() - 1);
        assertThat(testLeaveDeduction.getDays()).isEqualByComparingTo(DEFAULT_DAYS);
    }

    @Test
    @Transactional
    void createLeaveDeductionWithExistingId() throws Exception {
        // Create the LeaveDeduction with an existing ID
        leaveDeduction.setId(1L);
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        int databaseSizeBeforeCreate = leaveDeductionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveDeductionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveDeductionRepository.findAll().size();
        // set the field null
        leaveDeduction.setDays(null);

        // Create the LeaveDeduction, which fails.
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        restLeaveDeductionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLeaveDeductions() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveDeduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(sameNumber(DEFAULT_DAYS))));
    }

    @Test
    @Transactional
    void getLeaveDeduction() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get the leaveDeduction
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL_ID, leaveDeduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveDeduction.getId().intValue()))
            .andExpect(jsonPath("$.days").value(sameNumber(DEFAULT_DAYS)));
    }

    @Test
    @Transactional
    void getLeaveDeductionsByIdFiltering() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        Long id = leaveDeduction.getId();

        defaultLeaveDeductionShouldBeFound("id.equals=" + id);
        defaultLeaveDeductionShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveDeductionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveDeductionShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveDeductionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveDeductionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days equals to DEFAULT_DAYS
        defaultLeaveDeductionShouldBeFound("days.equals=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days equals to UPDATED_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.equals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days not equals to DEFAULT_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.notEquals=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days not equals to UPDATED_DAYS
        defaultLeaveDeductionShouldBeFound("days.notEquals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsInShouldWork() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days in DEFAULT_DAYS or UPDATED_DAYS
        defaultLeaveDeductionShouldBeFound("days.in=" + DEFAULT_DAYS + "," + UPDATED_DAYS);

        // Get all the leaveDeductionList where days equals to UPDATED_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.in=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days is not null
        defaultLeaveDeductionShouldBeFound("days.specified=true");

        // Get all the leaveDeductionList where days is null
        defaultLeaveDeductionShouldNotBeFound("days.specified=false");
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days is greater than or equal to DEFAULT_DAYS
        defaultLeaveDeductionShouldBeFound("days.greaterThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days is greater than or equal to UPDATED_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.greaterThanOrEqual=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days is less than or equal to DEFAULT_DAYS
        defaultLeaveDeductionShouldBeFound("days.lessThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days is less than or equal to SMALLER_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.lessThanOrEqual=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days is less than DEFAULT_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.lessThan=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days is less than UPDATED_DAYS
        defaultLeaveDeductionShouldBeFound("days.lessThan=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        // Get all the leaveDeductionList where days is greater than DEFAULT_DAYS
        defaultLeaveDeductionShouldNotBeFound("days.greaterThan=" + DEFAULT_DAYS);

        // Get all the leaveDeductionList where days is greater than SMALLER_DAYS
        defaultLeaveDeductionShouldBeFound("days.greaterThan=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByApplicationIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);
        LeaveApplication application = LeaveApplicationResourceIT.createEntity(em);
        em.persist(application);
        em.flush();
        leaveDeduction.setApplication(application);
        leaveDeductionRepository.saveAndFlush(leaveDeduction);
        Long applicationId = application.getId();

        // Get all the leaveDeductionList where application equals to applicationId
        defaultLeaveDeductionShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the leaveDeductionList where application equals to (applicationId + 1)
        defaultLeaveDeductionShouldNotBeFound("applicationId.equals=" + (applicationId + 1));
    }

    @Test
    @Transactional
    void getAllLeaveDeductionsByEntitlementIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);
        LeaveEntitlement entitlement = LeaveEntitlementResourceIT.createEntity(em);
        em.persist(entitlement);
        em.flush();
        leaveDeduction.setEntitlement(entitlement);
        leaveDeductionRepository.saveAndFlush(leaveDeduction);
        Long entitlementId = entitlement.getId();

        // Get all the leaveDeductionList where entitlement equals to entitlementId
        defaultLeaveDeductionShouldBeFound("entitlementId.equals=" + entitlementId);

        // Get all the leaveDeductionList where entitlement equals to (entitlementId + 1)
        defaultLeaveDeductionShouldNotBeFound("entitlementId.equals=" + (entitlementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveDeductionShouldBeFound(String filter) throws Exception {
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveDeduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(sameNumber(DEFAULT_DAYS))));

        // Check, that the count call also returns 1
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveDeductionShouldNotBeFound(String filter) throws Exception {
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveDeductionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLeaveDeduction() throws Exception {
        // Get the leaveDeduction
        restLeaveDeductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLeaveDeduction() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();

        // Update the leaveDeduction
        LeaveDeduction updatedLeaveDeduction = leaveDeductionRepository.findById(leaveDeduction.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveDeduction are not directly saved in db
        em.detach(updatedLeaveDeduction);
        updatedLeaveDeduction.days(UPDATED_DAYS);
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(updatedLeaveDeduction);

        restLeaveDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveDeductionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
        LeaveDeduction testLeaveDeduction = leaveDeductionList.get(leaveDeductionList.size() - 1);
        assertThat(testLeaveDeduction.getDays()).isEqualTo(UPDATED_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, leaveDeductionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeaveDeductionWithPatch() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();

        // Update the leaveDeduction using partial update
        LeaveDeduction partialUpdatedLeaveDeduction = new LeaveDeduction();
        partialUpdatedLeaveDeduction.setId(leaveDeduction.getId());

        restLeaveDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveDeduction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveDeduction))
            )
            .andExpect(status().isOk());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
        LeaveDeduction testLeaveDeduction = leaveDeductionList.get(leaveDeductionList.size() - 1);
        assertThat(testLeaveDeduction.getDays()).isEqualByComparingTo(DEFAULT_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateLeaveDeductionWithPatch() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();

        // Update the leaveDeduction using partial update
        LeaveDeduction partialUpdatedLeaveDeduction = new LeaveDeduction();
        partialUpdatedLeaveDeduction.setId(leaveDeduction.getId());

        partialUpdatedLeaveDeduction.days(UPDATED_DAYS);

        restLeaveDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLeaveDeduction.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLeaveDeduction))
            )
            .andExpect(status().isOk());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
        LeaveDeduction testLeaveDeduction = leaveDeductionList.get(leaveDeductionList.size() - 1);
        assertThat(testLeaveDeduction.getDays()).isEqualByComparingTo(UPDATED_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, leaveDeductionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLeaveDeduction() throws Exception {
        int databaseSizeBeforeUpdate = leaveDeductionRepository.findAll().size();
        leaveDeduction.setId(count.incrementAndGet());

        // Create the LeaveDeduction
        LeaveDeductionDTO leaveDeductionDTO = leaveDeductionMapper.toDto(leaveDeduction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeaveDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(leaveDeductionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LeaveDeduction in the database
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLeaveDeduction() throws Exception {
        // Initialize the database
        leaveDeductionRepository.saveAndFlush(leaveDeduction);

        int databaseSizeBeforeDelete = leaveDeductionRepository.findAll().size();

        // Delete the leaveDeduction
        restLeaveDeductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, leaveDeduction.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveDeduction> leaveDeductionList = leaveDeductionRepository.findAll();
        assertThat(leaveDeductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
