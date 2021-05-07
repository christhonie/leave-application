package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.LeaveApplicationApp;
import za.co.dearx.leave.domain.LeaveEntitlement;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveEntitlementRepository;
import za.co.dearx.leave.service.LeaveEntitlementQueryService;
import za.co.dearx.leave.service.LeaveEntitlementService;
import za.co.dearx.leave.service.dto.LeaveEntitlementCriteria;
import za.co.dearx.leave.service.dto.LeaveEntitlementDTO;
import za.co.dearx.leave.service.mapper.LeaveEntitlementMapper;

/**
 * Integration tests for the {@link LeaveEntitlementResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeaveEntitlementResourceIT {
    private static final LocalDate DEFAULT_ENTITLEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTITLEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENTITLEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_DAYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DAYS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DAYS = new BigDecimal(1 - 1);

    @Autowired
    private LeaveEntitlementRepository leaveEntitlementRepository;

    @Autowired
    private LeaveEntitlementMapper leaveEntitlementMapper;

    @Autowired
    private LeaveEntitlementService leaveEntitlementService;

    @Autowired
    private LeaveEntitlementQueryService leaveEntitlementQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveEntitlementMockMvc;

    private LeaveEntitlement leaveEntitlement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveEntitlement createEntity(EntityManager em) {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement().entitlementDate(DEFAULT_ENTITLEMENT_DATE).days(DEFAULT_DAYS);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leaveEntitlement.setLeaveType(leaveType);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        leaveEntitlement.setStaff(staff);
        return leaveEntitlement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveEntitlement createUpdatedEntity(EntityManager em) {
        LeaveEntitlement leaveEntitlement = new LeaveEntitlement().entitlementDate(UPDATED_ENTITLEMENT_DATE).days(UPDATED_DAYS);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createUpdatedEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leaveEntitlement.setLeaveType(leaveType);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createUpdatedEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        leaveEntitlement.setStaff(staff);
        return leaveEntitlement;
    }

    @BeforeEach
    public void initTest() {
        leaveEntitlement = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveEntitlement() throws Exception {
        int databaseSizeBeforeCreate = leaveEntitlementRepository.findAll().size();
        // Create the LeaveEntitlement
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(leaveEntitlement);
        restLeaveEntitlementMockMvc
            .perform(
                post("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveEntitlement in the database
        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveEntitlement testLeaveEntitlement = leaveEntitlementList.get(leaveEntitlementList.size() - 1);
        assertThat(testLeaveEntitlement.getEntitlementDate()).isEqualTo(DEFAULT_ENTITLEMENT_DATE);
        assertThat(testLeaveEntitlement.getDays()).isEqualTo(DEFAULT_DAYS);
    }

    @Test
    @Transactional
    public void createLeaveEntitlementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveEntitlementRepository.findAll().size();

        // Create the LeaveEntitlement with an existing ID
        leaveEntitlement.setId(1L);
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(leaveEntitlement);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveEntitlementMockMvc
            .perform(
                post("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveEntitlement in the database
        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntitlementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveEntitlementRepository.findAll().size();
        // set the field null
        leaveEntitlement.setEntitlementDate(null);

        // Create the LeaveEntitlement, which fails.
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(leaveEntitlement);

        restLeaveEntitlementMockMvc
            .perform(
                post("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveEntitlementRepository.findAll().size();
        // set the field null
        leaveEntitlement.setDays(null);

        // Create the LeaveEntitlement, which fails.
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(leaveEntitlement);

        restLeaveEntitlementMockMvc
            .perform(
                post("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlements() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveEntitlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].entitlementDate").value(hasItem(DEFAULT_ENTITLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS.intValue())));
    }

    @Test
    @Transactional
    public void getLeaveEntitlement() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get the leaveEntitlement
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements/{id}", leaveEntitlement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveEntitlement.getId().intValue()))
            .andExpect(jsonPath("$.entitlementDate").value(DEFAULT_ENTITLEMENT_DATE.toString()))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS.intValue()));
    }

    @Test
    @Transactional
    public void getLeaveEntitlementsByIdFiltering() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        Long id = leaveEntitlement.getId();

        defaultLeaveEntitlementShouldBeFound("id.equals=" + id);
        defaultLeaveEntitlementShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveEntitlementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveEntitlementShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveEntitlementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveEntitlementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate equals to DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.equals=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate equals to UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.equals=" + UPDATED_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate not equals to DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.notEquals=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate not equals to UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.notEquals=" + UPDATED_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate in DEFAULT_ENTITLEMENT_DATE or UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.in=" + DEFAULT_ENTITLEMENT_DATE + "," + UPDATED_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate equals to UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.in=" + UPDATED_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate is not null
        defaultLeaveEntitlementShouldBeFound("entitlementDate.specified=true");

        // Get all the leaveEntitlementList where entitlementDate is null
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate is greater than or equal to DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.greaterThanOrEqual=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate is greater than or equal to UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.greaterThanOrEqual=" + UPDATED_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate is less than or equal to DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.lessThanOrEqual=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate is less than or equal to SMALLER_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.lessThanOrEqual=" + SMALLER_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate is less than DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.lessThan=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate is less than UPDATED_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.lessThan=" + UPDATED_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByEntitlementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where entitlementDate is greater than DEFAULT_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldNotBeFound("entitlementDate.greaterThan=" + DEFAULT_ENTITLEMENT_DATE);

        // Get all the leaveEntitlementList where entitlementDate is greater than SMALLER_ENTITLEMENT_DATE
        defaultLeaveEntitlementShouldBeFound("entitlementDate.greaterThan=" + SMALLER_ENTITLEMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days equals to DEFAULT_DAYS
        defaultLeaveEntitlementShouldBeFound("days.equals=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days equals to UPDATED_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.equals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days not equals to DEFAULT_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.notEquals=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days not equals to UPDATED_DAYS
        defaultLeaveEntitlementShouldBeFound("days.notEquals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsInShouldWork() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days in DEFAULT_DAYS or UPDATED_DAYS
        defaultLeaveEntitlementShouldBeFound("days.in=" + DEFAULT_DAYS + "," + UPDATED_DAYS);

        // Get all the leaveEntitlementList where days equals to UPDATED_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.in=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days is not null
        defaultLeaveEntitlementShouldBeFound("days.specified=true");

        // Get all the leaveEntitlementList where days is null
        defaultLeaveEntitlementShouldNotBeFound("days.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days is greater than or equal to DEFAULT_DAYS
        defaultLeaveEntitlementShouldBeFound("days.greaterThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days is greater than or equal to UPDATED_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.greaterThanOrEqual=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days is less than or equal to DEFAULT_DAYS
        defaultLeaveEntitlementShouldBeFound("days.lessThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days is less than or equal to SMALLER_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.lessThanOrEqual=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days is less than DEFAULT_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.lessThan=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days is less than UPDATED_DAYS
        defaultLeaveEntitlementShouldBeFound("days.lessThan=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        // Get all the leaveEntitlementList where days is greater than DEFAULT_DAYS
        defaultLeaveEntitlementShouldNotBeFound("days.greaterThan=" + DEFAULT_DAYS);

        // Get all the leaveEntitlementList where days is greater than SMALLER_DAYS
        defaultLeaveEntitlementShouldBeFound("days.greaterThan=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByLeaveTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        LeaveType leaveType = leaveEntitlement.getLeaveType();
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);
        Long leaveTypeId = leaveType.getId();

        // Get all the leaveEntitlementList where leaveType equals to leaveTypeId
        defaultLeaveEntitlementShouldBeFound("leaveTypeId.equals=" + leaveTypeId);

        // Get all the leaveEntitlementList where leaveType equals to leaveTypeId + 1
        defaultLeaveEntitlementShouldNotBeFound("leaveTypeId.equals=" + (leaveTypeId + 1));
    }

    @Test
    @Transactional
    public void getAllLeaveEntitlementsByStaffIsEqualToSomething() throws Exception {
        // Get already existing entity
        Staff staff = leaveEntitlement.getStaff();
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);
        Long staffId = staff.getId();

        // Get all the leaveEntitlementList where staff equals to staffId
        defaultLeaveEntitlementShouldBeFound("staffId.equals=" + staffId);

        // Get all the leaveEntitlementList where staff equals to staffId + 1
        defaultLeaveEntitlementShouldNotBeFound("staffId.equals=" + (staffId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveEntitlementShouldBeFound(String filter) throws Exception {
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveEntitlement.getId().intValue())))
            .andExpect(jsonPath("$.[*].entitlementDate").value(hasItem(DEFAULT_ENTITLEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS.intValue())));

        // Check, that the count call also returns 1
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveEntitlementShouldNotBeFound(String filter) throws Exception {
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveEntitlementMockMvc
            .perform(get("/api/leave-entitlements/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeaveEntitlement() throws Exception {
        // Get the leaveEntitlement
        restLeaveEntitlementMockMvc.perform(get("/api/leave-entitlements/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveEntitlement() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        int databaseSizeBeforeUpdate = leaveEntitlementRepository.findAll().size();

        // Update the leaveEntitlement
        LeaveEntitlement updatedLeaveEntitlement = leaveEntitlementRepository.findById(leaveEntitlement.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveEntitlement are not directly saved in db
        em.detach(updatedLeaveEntitlement);
        updatedLeaveEntitlement.entitlementDate(UPDATED_ENTITLEMENT_DATE).days(UPDATED_DAYS);
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(updatedLeaveEntitlement);

        restLeaveEntitlementMockMvc
            .perform(
                put("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveEntitlement in the database
        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeUpdate);
        LeaveEntitlement testLeaveEntitlement = leaveEntitlementList.get(leaveEntitlementList.size() - 1);
        assertThat(testLeaveEntitlement.getEntitlementDate()).isEqualTo(UPDATED_ENTITLEMENT_DATE);
        assertThat(testLeaveEntitlement.getDays()).isEqualTo(UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveEntitlement() throws Exception {
        int databaseSizeBeforeUpdate = leaveEntitlementRepository.findAll().size();

        // Create the LeaveEntitlement
        LeaveEntitlementDTO leaveEntitlementDTO = leaveEntitlementMapper.toDto(leaveEntitlement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveEntitlementMockMvc
            .perform(
                put("/api/leave-entitlements")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveEntitlementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveEntitlement in the database
        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveEntitlement() throws Exception {
        // Initialize the database
        leaveEntitlementRepository.saveAndFlush(leaveEntitlement);

        int databaseSizeBeforeDelete = leaveEntitlementRepository.findAll().size();

        // Delete the leaveEntitlement
        restLeaveEntitlementMockMvc
            .perform(delete("/api/leave-entitlements/{id}", leaveEntitlement.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveEntitlement> leaveEntitlementList = leaveEntitlementRepository.findAll();
        assertThat(leaveEntitlementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
