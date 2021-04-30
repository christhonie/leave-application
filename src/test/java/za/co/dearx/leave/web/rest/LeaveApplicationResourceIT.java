package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static za.co.dearx.leave.web.rest.TestUtil.sameInstant;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.repository.LeaveApplicationRepository;
import za.co.dearx.leave.service.LeaveApplicationQueryService;
import za.co.dearx.leave.service.LeaveApplicationService;
import za.co.dearx.leave.service.dto.LeaveApplicationCriteria;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.mapper.LeaveApplicationMapper;

/**
 * Integration tests for the {@link LeaveApplicationResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeaveApplicationResourceIT {
    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_APPLIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_APPLIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_APPLIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_UPDATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_DAYS = new BigDecimal(1);
    private static final BigDecimal UPDATED_DAYS = new BigDecimal(2);
    private static final BigDecimal SMALLER_DAYS = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;

    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @Autowired
    private LeaveApplicationQueryService leaveApplicationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveApplicationMockMvc;

    private LeaveApplication leaveApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveApplication createEntity(EntityManager em) {
        LeaveApplication leaveApplication = new LeaveApplication()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .appliedDate(DEFAULT_APPLIED_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .days(DEFAULT_DAYS)
            .deleted(DEFAULT_DELETED);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leaveApplication.setLeaveType(leaveType);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        leaveApplication.setStaff(staff);
        return leaveApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveApplication createUpdatedEntity(EntityManager em) {
        LeaveApplication leaveApplication = new LeaveApplication()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .appliedDate(UPDATED_APPLIED_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .days(UPDATED_DAYS)
            .deleted(UPDATED_DELETED);
        // Add required entity
        LeaveType leaveType;
        if (TestUtil.findAll(em, LeaveType.class).isEmpty()) {
            leaveType = LeaveTypeResourceIT.createUpdatedEntity(em);
            em.persist(leaveType);
            em.flush();
        } else {
            leaveType = TestUtil.findAll(em, LeaveType.class).get(0);
        }
        leaveApplication.setLeaveType(leaveType);
        // Add required entity
        Staff staff;
        if (TestUtil.findAll(em, Staff.class).isEmpty()) {
            staff = StaffResourceIT.createUpdatedEntity(em);
            em.persist(staff);
            em.flush();
        } else {
            staff = TestUtil.findAll(em, Staff.class).get(0);
        }
        leaveApplication.setStaff(staff);
        return leaveApplication;
    }

    @BeforeEach
    public void initTest() {
        leaveApplication = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveApplication() throws Exception {
        int databaseSizeBeforeCreate = leaveApplicationRepository.findAll().size();
        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);
        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLeaveApplication.getAppliedDate()).isEqualTo(DEFAULT_APPLIED_DATE);
        assertThat(testLeaveApplication.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testLeaveApplication.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testLeaveApplication.isDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    @Transactional
    public void createLeaveApplicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveApplicationRepository.findAll().size();

        // Create the LeaveApplication with an existing ID
        leaveApplication.setId(1L);
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveApplicationRepository.findAll().size();
        // set the field null
        leaveApplication.setStartDate(null);

        // Create the LeaveApplication, which fails.
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveApplicationRepository.findAll().size();
        // set the field null
        leaveApplication.setEndDate(null);

        // Create the LeaveApplication, which fails.
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppliedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveApplicationRepository.findAll().size();
        // set the field null
        leaveApplication.setAppliedDate(null);

        // Create the LeaveApplication, which fails.
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveApplicationRepository.findAll().size();
        // set the field null
        leaveApplication.setDays(null);

        // Create the LeaveApplication, which fails.
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDeletedIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveApplicationRepository.findAll().size();
        // set the field null
        leaveApplication.setDeleted(null);

        // Create the LeaveApplication, which fails.
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                post("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaveApplications() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].appliedDate").value(hasItem(sameInstant(DEFAULT_APPLIED_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS.intValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get the leaveApplication
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications/{id}", leaveApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveApplication.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.appliedDate").value(sameInstant(DEFAULT_APPLIED_DATE)))
            .andExpect(jsonPath("$.updateDate").value(sameInstant(DEFAULT_UPDATE_DATE)))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS.intValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getLeaveApplicationsByIdFiltering() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        Long id = leaveApplication.getId();

        defaultLeaveApplicationShouldBeFound("id.equals=" + id);
        defaultLeaveApplicationShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveApplicationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveApplicationShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveApplicationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveApplicationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate equals to DEFAULT_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate equals to UPDATED_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate not equals to DEFAULT_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate not equals to UPDATED_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the leaveApplicationList where startDate equals to UPDATED_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate is not null
        defaultLeaveApplicationShouldBeFound("startDate.specified=true");

        // Get all the leaveApplicationList where startDate is null
        defaultLeaveApplicationShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate is greater than or equal to UPDATED_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate is less than or equal to DEFAULT_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate is less than or equal to SMALLER_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate is less than DEFAULT_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate is less than UPDATED_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where startDate is greater than DEFAULT_START_DATE
        defaultLeaveApplicationShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the leaveApplicationList where startDate is greater than SMALLER_START_DATE
        defaultLeaveApplicationShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate equals to DEFAULT_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate equals to UPDATED_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate not equals to DEFAULT_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate not equals to UPDATED_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the leaveApplicationList where endDate equals to UPDATED_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate is not null
        defaultLeaveApplicationShouldBeFound("endDate.specified=true");

        // Get all the leaveApplicationList where endDate is null
        defaultLeaveApplicationShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate is greater than or equal to DEFAULT_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate is greater than or equal to UPDATED_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate is less than or equal to DEFAULT_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.lessThanOrEqual=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate is less than or equal to SMALLER_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate is less than DEFAULT_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate is less than UPDATED_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where endDate is greater than DEFAULT_END_DATE
        defaultLeaveApplicationShouldNotBeFound("endDate.greaterThan=" + DEFAULT_END_DATE);

        // Get all the leaveApplicationList where endDate is greater than SMALLER_END_DATE
        defaultLeaveApplicationShouldBeFound("endDate.greaterThan=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate equals to DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.equals=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate equals to UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.equals=" + UPDATED_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate not equals to DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.notEquals=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate not equals to UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.notEquals=" + UPDATED_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate in DEFAULT_APPLIED_DATE or UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.in=" + DEFAULT_APPLIED_DATE + "," + UPDATED_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate equals to UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.in=" + UPDATED_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate is not null
        defaultLeaveApplicationShouldBeFound("appliedDate.specified=true");

        // Get all the leaveApplicationList where appliedDate is null
        defaultLeaveApplicationShouldNotBeFound("appliedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate is greater than or equal to DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.greaterThanOrEqual=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate is greater than or equal to UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.greaterThanOrEqual=" + UPDATED_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate is less than or equal to DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.lessThanOrEqual=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate is less than or equal to SMALLER_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.lessThanOrEqual=" + SMALLER_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate is less than DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.lessThan=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate is less than UPDATED_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.lessThan=" + UPDATED_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByAppliedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where appliedDate is greater than DEFAULT_APPLIED_DATE
        defaultLeaveApplicationShouldNotBeFound("appliedDate.greaterThan=" + DEFAULT_APPLIED_DATE);

        // Get all the leaveApplicationList where appliedDate is greater than SMALLER_APPLIED_DATE
        defaultLeaveApplicationShouldBeFound("appliedDate.greaterThan=" + SMALLER_APPLIED_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate equals to DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.equals=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate equals to UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.equals=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate not equals to DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.notEquals=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate not equals to UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.notEquals=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate in DEFAULT_UPDATE_DATE or UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.in=" + DEFAULT_UPDATE_DATE + "," + UPDATED_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate equals to UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.in=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate is not null
        defaultLeaveApplicationShouldBeFound("updateDate.specified=true");

        // Get all the leaveApplicationList where updateDate is null
        defaultLeaveApplicationShouldNotBeFound("updateDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate is greater than or equal to DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.greaterThanOrEqual=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate is greater than or equal to UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.greaterThanOrEqual=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate is less than or equal to DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.lessThanOrEqual=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate is less than or equal to SMALLER_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.lessThanOrEqual=" + SMALLER_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate is less than DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.lessThan=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate is less than UPDATED_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.lessThan=" + UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByUpdateDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where updateDate is greater than DEFAULT_UPDATE_DATE
        defaultLeaveApplicationShouldNotBeFound("updateDate.greaterThan=" + DEFAULT_UPDATE_DATE);

        // Get all the leaveApplicationList where updateDate is greater than SMALLER_UPDATE_DATE
        defaultLeaveApplicationShouldBeFound("updateDate.greaterThan=" + SMALLER_UPDATE_DATE);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days equals to DEFAULT_DAYS
        defaultLeaveApplicationShouldBeFound("days.equals=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days equals to UPDATED_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.equals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days not equals to DEFAULT_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.notEquals=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days not equals to UPDATED_DAYS
        defaultLeaveApplicationShouldBeFound("days.notEquals=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days in DEFAULT_DAYS or UPDATED_DAYS
        defaultLeaveApplicationShouldBeFound("days.in=" + DEFAULT_DAYS + "," + UPDATED_DAYS);

        // Get all the leaveApplicationList where days equals to UPDATED_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.in=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days is not null
        defaultLeaveApplicationShouldBeFound("days.specified=true");

        // Get all the leaveApplicationList where days is null
        defaultLeaveApplicationShouldNotBeFound("days.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days is greater than or equal to DEFAULT_DAYS
        defaultLeaveApplicationShouldBeFound("days.greaterThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days is greater than or equal to UPDATED_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.greaterThanOrEqual=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days is less than or equal to DEFAULT_DAYS
        defaultLeaveApplicationShouldBeFound("days.lessThanOrEqual=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days is less than or equal to SMALLER_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.lessThanOrEqual=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days is less than DEFAULT_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.lessThan=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days is less than UPDATED_DAYS
        defaultLeaveApplicationShouldBeFound("days.lessThan=" + UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where days is greater than DEFAULT_DAYS
        defaultLeaveApplicationShouldNotBeFound("days.greaterThan=" + DEFAULT_DAYS);

        // Get all the leaveApplicationList where days is greater than SMALLER_DAYS
        defaultLeaveApplicationShouldBeFound("days.greaterThan=" + SMALLER_DAYS);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where deleted equals to DEFAULT_DELETED
        defaultLeaveApplicationShouldBeFound("deleted.equals=" + DEFAULT_DELETED);

        // Get all the leaveApplicationList where deleted equals to UPDATED_DELETED
        defaultLeaveApplicationShouldNotBeFound("deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDeletedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where deleted not equals to DEFAULT_DELETED
        defaultLeaveApplicationShouldNotBeFound("deleted.notEquals=" + DEFAULT_DELETED);

        // Get all the leaveApplicationList where deleted not equals to UPDATED_DELETED
        defaultLeaveApplicationShouldBeFound("deleted.notEquals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where deleted in DEFAULT_DELETED or UPDATED_DELETED
        defaultLeaveApplicationShouldBeFound("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED);

        // Get all the leaveApplicationList where deleted equals to UPDATED_DELETED
        defaultLeaveApplicationShouldNotBeFound("deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        // Get all the leaveApplicationList where deleted is not null
        defaultLeaveApplicationShouldBeFound("deleted.specified=true");

        // Get all the leaveApplicationList where deleted is null
        defaultLeaveApplicationShouldNotBeFound("deleted.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByLeaveTypeIsEqualToSomething() throws Exception {
        // Get already existing entity
        LeaveType leaveType = leaveApplication.getLeaveType();
        leaveApplicationRepository.saveAndFlush(leaveApplication);
        Long leaveTypeId = leaveType.getId();

        // Get all the leaveApplicationList where leaveType equals to leaveTypeId
        defaultLeaveApplicationShouldBeFound("leaveTypeId.equals=" + leaveTypeId);

        // Get all the leaveApplicationList where leaveType equals to leaveTypeId + 1
        defaultLeaveApplicationShouldNotBeFound("leaveTypeId.equals=" + (leaveTypeId + 1));
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByLeaveStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);
        LeaveStatus leaveStatus = LeaveStatusResourceIT.createEntity(em);
        em.persist(leaveStatus);
        em.flush();
        leaveApplication.setLeaveStatus(leaveStatus);
        leaveApplicationRepository.saveAndFlush(leaveApplication);
        Long leaveStatusId = leaveStatus.getId();

        // Get all the leaveApplicationList where leaveStatus equals to leaveStatusId
        defaultLeaveApplicationShouldBeFound("leaveStatusId.equals=" + leaveStatusId);

        // Get all the leaveApplicationList where leaveStatus equals to leaveStatusId + 1
        defaultLeaveApplicationShouldNotBeFound("leaveStatusId.equals=" + (leaveStatusId + 1));
    }

    @Test
    @Transactional
    public void getAllLeaveApplicationsByStaffIsEqualToSomething() throws Exception {
        // Get already existing entity
        Staff staff = leaveApplication.getStaff();
        leaveApplicationRepository.saveAndFlush(leaveApplication);
        Long staffId = staff.getId();

        // Get all the leaveApplicationList where staff equals to staffId
        defaultLeaveApplicationShouldBeFound("staffId.equals=" + staffId);

        // Get all the leaveApplicationList where staff equals to staffId + 1
        defaultLeaveApplicationShouldNotBeFound("staffId.equals=" + (staffId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveApplicationShouldBeFound(String filter) throws Exception {
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].appliedDate").value(hasItem(sameInstant(DEFAULT_APPLIED_DATE))))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(sameInstant(DEFAULT_UPDATE_DATE))))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS.intValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveApplicationShouldNotBeFound(String filter) throws Exception {
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveApplicationMockMvc
            .perform(get("/api/leave-applications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeaveApplication() throws Exception {
        // Get the leaveApplication
        restLeaveApplicationMockMvc.perform(get("/api/leave-applications/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();

        // Update the leaveApplication
        LeaveApplication updatedLeaveApplication = leaveApplicationRepository.findById(leaveApplication.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveApplication are not directly saved in db
        em.detach(updatedLeaveApplication);
        updatedLeaveApplication
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .appliedDate(UPDATED_APPLIED_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .days(UPDATED_DAYS)
            .deleted(UPDATED_DELETED);
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(updatedLeaveApplication);

        restLeaveApplicationMockMvc
            .perform(
                put("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
        LeaveApplication testLeaveApplication = leaveApplicationList.get(leaveApplicationList.size() - 1);
        assertThat(testLeaveApplication.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLeaveApplication.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLeaveApplication.getAppliedDate()).isEqualTo(UPDATED_APPLIED_DATE);
        assertThat(testLeaveApplication.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testLeaveApplication.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testLeaveApplication.isDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveApplication() throws Exception {
        int databaseSizeBeforeUpdate = leaveApplicationRepository.findAll().size();

        // Create the LeaveApplication
        LeaveApplicationDTO leaveApplicationDTO = leaveApplicationMapper.toDto(leaveApplication);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveApplicationMockMvc
            .perform(
                put("/api/leave-applications")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveApplicationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveApplication in the database
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveApplication() throws Exception {
        // Initialize the database
        leaveApplicationRepository.saveAndFlush(leaveApplication);

        int databaseSizeBeforeDelete = leaveApplicationRepository.findAll().size();

        // Delete the leaveApplication
        restLeaveApplicationMockMvc
            .perform(delete("/api/leave-applications/{id}", leaveApplication.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveApplication> leaveApplicationList = leaveApplicationRepository.findAll();
        assertThat(leaveApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
