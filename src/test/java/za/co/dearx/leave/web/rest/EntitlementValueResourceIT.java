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
import za.co.dearx.leave.domain.EntitlementValue;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.EntitlementValueRepository;
import za.co.dearx.leave.service.criteria.EntitlementValueCriteria;
import za.co.dearx.leave.service.dto.EntitlementValueDTO;
import za.co.dearx.leave.service.mapper.EntitlementValueMapper;

/**
 * Integration tests for the {@link EntitlementValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntitlementValueResourceIT {

    private static final BigDecimal DEFAULT_ENTITLEMENT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ENTITLEMENT_VALUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ENTITLEMENT_VALUE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/entitlement-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntitlementValueRepository entitlementValueRepository;

    @Autowired
    private EntitlementValueMapper entitlementValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntitlementValueMockMvc;

    private EntitlementValue entitlementValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntitlementValue createEntity(EntityManager em) {
        EntitlementValue entitlementValue = new EntitlementValue().entitlementValue(DEFAULT_ENTITLEMENT_VALUE);
        // Add required entity
        LeaveEntitlement leaveEntitlement;
        if (TestUtil.findAll(em, LeaveEntitlement.class).isEmpty()) {
            leaveEntitlement = LeaveEntitlementResourceIT.createEntity(em);
            em.persist(leaveEntitlement);
            em.flush();
        } else {
            leaveEntitlement = TestUtil.findAll(em, LeaveEntitlement.class).get(0);
        }
        entitlementValue.setEntitlement(leaveEntitlement);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        entitlementValue.setStaff(staff);
        return entitlementValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntitlementValue createUpdatedEntity(EntityManager em) {
        EntitlementValue entitlementValue = new EntitlementValue().entitlementValue(UPDATED_ENTITLEMENT_VALUE);
        // Add required entity
        LeaveEntitlement leaveEntitlement;
        if (TestUtil.findAll(em, LeaveEntitlement.class).isEmpty()) {
            leaveEntitlement = LeaveEntitlementResourceIT.createUpdatedEntity(em);
            em.persist(leaveEntitlement);
            em.flush();
        } else {
            leaveEntitlement = TestUtil.findAll(em, LeaveEntitlement.class).get(0);
        }
        entitlementValue.setEntitlement(leaveEntitlement);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createUpdatedEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        entitlementValue.setStaff(staff);
        return entitlementValue;
    }

    @BeforeEach
    public void initTest() {
        entitlementValue = createEntity(em);
    }

    @Test
    @Transactional
    void createEntitlementValue() throws Exception {
        int databaseSizeBeforeCreate = entitlementValueRepository.findAll().size();
        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);
        restEntitlementValueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeCreate + 1);
        EntitlementValue testEntitlementValue = entitlementValueList.get(entitlementValueList.size() - 1);
        assertThat(testEntitlementValue.getEntitlementValue()).isEqualByComparingTo(DEFAULT_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void createEntitlementValueWithExistingId() throws Exception {
        // Create the EntitlementValue with an existing ID
        entitlementValue.setId(1L);
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        int databaseSizeBeforeCreate = entitlementValueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntitlementValueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntitlementValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = entitlementValueRepository.findAll().size();
        // set the field null
        entitlementValue.setEntitlementValue(null);

        // Create the EntitlementValue, which fails.
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        restEntitlementValueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntitlementValues() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entitlementValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].entitlementValue").value(hasItem(sameNumber(DEFAULT_ENTITLEMENT_VALUE))));
    }

    @Test
    @Transactional
    void getEntitlementValue() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get the entitlementValue
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL_ID, entitlementValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entitlementValue.getId().intValue()))
            .andExpect(jsonPath("$.entitlementValue").value(sameNumber(DEFAULT_ENTITLEMENT_VALUE)));
    }

    @Test
    @Transactional
    void getEntitlementValuesByIdFiltering() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        Long id = entitlementValue.getId();

        defaultEntitlementValueShouldBeFound("id.equals=" + id);
        defaultEntitlementValueShouldNotBeFound("id.notEquals=" + id);

        defaultEntitlementValueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEntitlementValueShouldNotBeFound("id.greaterThan=" + id);

        defaultEntitlementValueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEntitlementValueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsEqualToSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue equals to DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.equals=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue equals to UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.equals=" + UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue not equals to DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.notEquals=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue not equals to UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.notEquals=" + UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsInShouldWork() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue in DEFAULT_ENTITLEMENT_VALUE or UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.in=" + DEFAULT_ENTITLEMENT_VALUE + "," + UPDATED_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue equals to UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.in=" + UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue is not null
        defaultEntitlementValueShouldBeFound("entitlementValue.specified=true");

        // Get all the entitlementValueList where entitlementValue is null
        defaultEntitlementValueShouldNotBeFound("entitlementValue.specified=false");
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue is greater than or equal to DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.greaterThanOrEqual=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue is greater than or equal to UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.greaterThanOrEqual=" + UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue is less than or equal to DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.lessThanOrEqual=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue is less than or equal to SMALLER_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.lessThanOrEqual=" + SMALLER_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsLessThanSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue is less than DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.lessThan=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue is less than UPDATED_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.lessThan=" + UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        // Get all the entitlementValueList where entitlementValue is greater than DEFAULT_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldNotBeFound("entitlementValue.greaterThan=" + DEFAULT_ENTITLEMENT_VALUE);

        // Get all the entitlementValueList where entitlementValue is greater than SMALLER_ENTITLEMENT_VALUE
        defaultEntitlementValueShouldBeFound("entitlementValue.greaterThan=" + SMALLER_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByEntitlementIsEqualToSomething() throws Exception {
        // Get already existing entity
        LeaveEntitlement entitlement = entitlementValue.getEntitlement();
        entitlementValueRepository.saveAndFlush(entitlementValue);
        Long entitlementId = entitlement.getId();

        // Get all the entitlementValueList where entitlement equals to entitlementId
        defaultEntitlementValueShouldBeFound("entitlementId.equals=" + entitlementId);

        // Get all the entitlementValueList where entitlement equals to (entitlementId + 1)
        defaultEntitlementValueShouldNotBeFound("entitlementId.equals=" + (entitlementId + 1));
    }

    @Test
    @Transactional
    void getAllEntitlementValuesByStaffIsEqualToSomething() throws Exception {
        // Get already existing entity
        Staff staff = entitlementValue.getStaff();
        entitlementValueRepository.saveAndFlush(entitlementValue);
        Long staffId = staff.getId();

        // Get all the entitlementValueList where staff equals to staffId
        defaultEntitlementValueShouldBeFound("staffId.equals=" + staffId);

        // Get all the entitlementValueList where staff equals to (staffId + 1)
        defaultEntitlementValueShouldNotBeFound("staffId.equals=" + (staffId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEntitlementValueShouldBeFound(String filter) throws Exception {
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entitlementValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].entitlementValue").value(hasItem(sameNumber(DEFAULT_ENTITLEMENT_VALUE))));

        // Check, that the count call also returns 1
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEntitlementValueShouldNotBeFound(String filter) throws Exception {
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEntitlementValueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEntitlementValue() throws Exception {
        // Get the entitlementValue
        restEntitlementValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEntitlementValue() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();

        // Update the entitlementValue
        EntitlementValue updatedEntitlementValue = entitlementValueRepository.findById(entitlementValue.getId()).get();
        // Disconnect from session so that the updates on updatedEntitlementValue are not directly saved in db
        em.detach(updatedEntitlementValue);
        updatedEntitlementValue.entitlementValue(UPDATED_ENTITLEMENT_VALUE);
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(updatedEntitlementValue);

        restEntitlementValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entitlementValueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
        EntitlementValue testEntitlementValue = entitlementValueList.get(entitlementValueList.size() - 1);
        assertThat(testEntitlementValue.getEntitlementValue()).isEqualTo(UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entitlementValueDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntitlementValueWithPatch() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();

        // Update the entitlementValue using partial update
        EntitlementValue partialUpdatedEntitlementValue = new EntitlementValue();
        partialUpdatedEntitlementValue.setId(entitlementValue.getId());

        restEntitlementValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntitlementValue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntitlementValue))
            )
            .andExpect(status().isOk());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
        EntitlementValue testEntitlementValue = entitlementValueList.get(entitlementValueList.size() - 1);
        assertThat(testEntitlementValue.getEntitlementValue()).isEqualByComparingTo(DEFAULT_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateEntitlementValueWithPatch() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();

        // Update the entitlementValue using partial update
        EntitlementValue partialUpdatedEntitlementValue = new EntitlementValue();
        partialUpdatedEntitlementValue.setId(entitlementValue.getId());

        partialUpdatedEntitlementValue.entitlementValue(UPDATED_ENTITLEMENT_VALUE);

        restEntitlementValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntitlementValue.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntitlementValue))
            )
            .andExpect(status().isOk());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
        EntitlementValue testEntitlementValue = entitlementValueList.get(entitlementValueList.size() - 1);
        assertThat(testEntitlementValue.getEntitlementValue()).isEqualByComparingTo(UPDATED_ENTITLEMENT_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entitlementValueDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntitlementValue() throws Exception {
        int databaseSizeBeforeUpdate = entitlementValueRepository.findAll().size();
        entitlementValue.setId(count.incrementAndGet());

        // Create the EntitlementValue
        EntitlementValueDTO entitlementValueDTO = entitlementValueMapper.toDto(entitlementValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntitlementValueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entitlementValueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EntitlementValue in the database
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntitlementValue() throws Exception {
        // Initialize the database
        entitlementValueRepository.saveAndFlush(entitlementValue);

        int databaseSizeBeforeDelete = entitlementValueRepository.findAll().size();

        // Delete the entitlementValue
        restEntitlementValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, entitlementValue.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EntitlementValue> entitlementValueList = entitlementValueRepository.findAll();
        assertThat(entitlementValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
