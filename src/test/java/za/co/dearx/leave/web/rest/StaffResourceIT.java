package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.IntegrationTest;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.domain.Team;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.repository.StaffRepository;
import za.co.dearx.leave.service.StaffService;
import za.co.dearx.leave.service.criteria.StaffCriteria;
import za.co.dearx.leave.service.dto.StaffDTO;
import za.co.dearx.leave.service.mapper.StaffMapper;

/**
 * Integration tests for the {@link StaffResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StaffResourceIT {

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = DEFAULT_FIRST_NAME + " " + DEFAULT_LAST_NAME;
    private static final String UPDATED_NAME = UPDATED_FIRST_NAME + " " + UPDATED_LAST_NAME;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AA";
    private static final String UPDATED_GENDER = "BB";

    private static final BigDecimal DEFAULT_ANNUAL_LEAVE_ENTITLEMENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ANNUAL_LEAVE_ENTITLEMENT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ANNUAL_LEAVE_ENTITLEMENT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/staff";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffRepository staffRepository;

    @Mock
    private StaffRepository staffRepositoryMock;

    @Autowired
    private StaffMapper staffMapper;

    @Mock
    private StaffService staffServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffMockMvc;

    private Staff staff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createEntity(EntityManager em) {
        Staff staff = new Staff()
            .position(DEFAULT_POSITION)
            .employeeID(DEFAULT_EMPLOYEE_ID)
            .startDate(DEFAULT_START_DATE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .gender(DEFAULT_GENDER)
            .annualLeaveEntitlement(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);
        return staff;
    }

    /**
     * Create an entity for this test and add a user to the entity before returning
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createEntityWithUser(EntityManager em) {
        Staff staff = new Staff()
            .position(DEFAULT_POSITION)
            .employeeID(DEFAULT_EMPLOYEE_ID)
            .startDate(DEFAULT_START_DATE)
            .name(DEFAULT_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .gender(DEFAULT_GENDER)
            .annualLeaveEntitlement(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);
        User user = UserResourceIT.createEntity(em);
        user.setLogin(UserResourceIT.DEFAULT_LOGIN);
        em.persist(user);
        em.flush();
        staff.setUser(user);
        return staff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createUpdatedEntity(EntityManager em) {
        Staff staff = new Staff()
            .position(UPDATED_POSITION)
            .employeeID(UPDATED_EMPLOYEE_ID)
            .startDate(UPDATED_START_DATE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .gender(UPDATED_GENDER)
            .annualLeaveEntitlement(UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
        return staff;
    }

    @BeforeEach
    public void initTest() {
        staff = createEntity(em);
    }

    @Test
    @Transactional
    void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();
        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);
        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate + 1);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testStaff.getEmployeeID()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testStaff.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaff.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStaff.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStaff.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStaff.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testStaff.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testStaff.getAnnualLeaveEntitlement()).isEqualTo(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void createStaffWithExistingId() throws Exception {
        // Create the Staff with an existing ID
        staff.setId(1L);
        StaffDTO staffDTO = staffMapper.toDto(staff);

        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmployeeIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setEmployeeID(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setStartDate(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setFirstName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setLastName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setGender(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].employeeID").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].annualLeaveEntitlement").value(hasItem(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffWithEagerRelationshipsIsEnabled() throws Exception {
        when(staffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStaffWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(staffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(staffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc
            .perform(get(ENTITY_API_URL_ID, staff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staff.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.employeeID").value(DEFAULT_EMPLOYEE_ID))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.annualLeaveEntitlement").value(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT.intValue()));
    }

    @Test
    @Transactional
    void getStaffByIdFiltering() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        Long id = staff.getId();

        defaultStaffShouldBeFound("id.equals=" + id);
        defaultStaffShouldNotBeFound("id.notEquals=" + id);

        defaultStaffShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStaffShouldNotBeFound("id.greaterThan=" + id);

        defaultStaffShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStaffShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStaffByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position equals to DEFAULT_POSITION
        defaultStaffShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the staffList where position equals to UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllStaffByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position not equals to DEFAULT_POSITION
        defaultStaffShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the staffList where position not equals to UPDATED_POSITION
        defaultStaffShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllStaffByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultStaffShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the staffList where position equals to UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllStaffByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position is not null
        defaultStaffShouldBeFound("position.specified=true");

        // Get all the staffList where position is null
        defaultStaffShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByPositionContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position contains DEFAULT_POSITION
        defaultStaffShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the staffList where position contains UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllStaffByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position does not contain DEFAULT_POSITION
        defaultStaffShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the staffList where position does not contain UPDATED_POSITION
        defaultStaffShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID equals to DEFAULT_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID not equals to DEFAULT_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID not equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the staffList where employeeID equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID is not null
        defaultStaffShouldBeFound("employeeID.specified=true");

        // Get all the staffList where employeeID is null
        defaultStaffShouldNotBeFound("employeeID.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID contains DEFAULT_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.contains=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID contains UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.contains=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllStaffByEmployeeIDNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID does not contain DEFAULT_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.doesNotContain=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID does not contain UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.doesNotContain=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate equals to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate equals to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate not equals to DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate not equals to UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the staffList where startDate equals to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is not null
        defaultStaffShouldBeFound("startDate.specified=true");

        // Get all the staffList where startDate is null
        defaultStaffShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is greater than or equal to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is less than or equal to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is less than or equal to SMALLER_START_DATE
        defaultStaffShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is less than DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is less than UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllStaffByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is greater than DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is greater than SMALLER_START_DATE
        defaultStaffShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName equals to DEFAULT_FIRST_NAME
        defaultStaffShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName equals to UPDATED_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName not equals to UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the staffList where firstName equals to UPDATED_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName is not null
        defaultStaffShouldBeFound("firstName.specified=true");

        // Get all the staffList where firstName is null
        defaultStaffShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName contains DEFAULT_FIRST_NAME
        defaultStaffShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName contains UPDATED_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName does not contain UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName equals to DEFAULT_LAST_NAME
        defaultStaffShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName equals to UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName not equals to DEFAULT_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName not equals to UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the staffList where lastName equals to UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName is not null
        defaultStaffShouldBeFound("lastName.specified=true");

        // Get all the staffList where lastName is null
        defaultStaffShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByLastNameContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName contains DEFAULT_LAST_NAME
        defaultStaffShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName contains UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName does not contain DEFAULT_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName does not contain UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllStaffByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email equals to DEFAULT_EMAIL
        defaultStaffShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the staffList where email equals to UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStaffByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email not equals to DEFAULT_EMAIL
        defaultStaffShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the staffList where email not equals to UPDATED_EMAIL
        defaultStaffShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStaffByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStaffShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the staffList where email equals to UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStaffByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email is not null
        defaultStaffShouldBeFound("email.specified=true");

        // Get all the staffList where email is null
        defaultStaffShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByEmailContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email contains DEFAULT_EMAIL
        defaultStaffShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the staffList where email contains UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStaffByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email does not contain DEFAULT_EMAIL
        defaultStaffShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the staffList where email does not contain UPDATED_EMAIL
        defaultStaffShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber equals to DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber not equals to DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.notEquals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber not equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.notEquals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber in DEFAULT_CONTRACT_NUMBER or UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.in=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber is not null
        defaultStaffShouldBeFound("contractNumber.specified=true");

        // Get all the staffList where contractNumber is null
        defaultStaffShouldNotBeFound("contractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber contains DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber contains UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.contains=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStaffByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber does not contain DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber does not contain UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllStaffByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender equals to DEFAULT_GENDER
        defaultStaffShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the staffList where gender equals to UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStaffByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender not equals to DEFAULT_GENDER
        defaultStaffShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the staffList where gender not equals to UPDATED_GENDER
        defaultStaffShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStaffByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultStaffShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the staffList where gender equals to UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStaffByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender is not null
        defaultStaffShouldBeFound("gender.specified=true");

        // Get all the staffList where gender is null
        defaultStaffShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByGenderContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender contains DEFAULT_GENDER
        defaultStaffShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the staffList where gender contains UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStaffByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender does not contain DEFAULT_GENDER
        defaultStaffShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the staffList where gender does not contain UPDATED_GENDER
        defaultStaffShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement equals to DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.equals=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement equals to UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.equals=" + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement not equals to DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.notEquals=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement not equals to UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.notEquals=" + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement in DEFAULT_ANNUAL_LEAVE_ENTITLEMENT or UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.in=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT + "," + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement equals to UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.in=" + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement is not null
        defaultStaffShouldBeFound("annualLeaveEntitlement.specified=true");

        // Get all the staffList where annualLeaveEntitlement is null
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.specified=false");
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement is greater than or equal to DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.greaterThanOrEqual=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement is greater than or equal to UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.greaterThanOrEqual=" + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement is less than or equal to DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.lessThanOrEqual=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement is less than or equal to SMALLER_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.lessThanOrEqual=" + SMALLER_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsLessThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement is less than DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.lessThan=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement is less than UPDATED_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.lessThan=" + UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByAnnualLeaveEntitlementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where annualLeaveEntitlement is greater than DEFAULT_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldNotBeFound("annualLeaveEntitlement.greaterThan=" + DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);

        // Get all the staffList where annualLeaveEntitlement is greater than SMALLER_ANNUAL_LEAVE_ENTITLEMENT
        defaultStaffShouldBeFound("annualLeaveEntitlement.greaterThan=" + SMALLER_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void getAllStaffByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        staff.setUser(user);
        staffRepository.saveAndFlush(staff);
        Long userId = user.getId();

        // Get all the staffList where user equals to userId
        defaultStaffShouldBeFound("userId.equals=" + userId);

        // Get all the staffList where user equals to (userId + 1)
        defaultStaffShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllStaffByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);
        Team team = TeamResourceIT.createEntity(em);
        em.persist(team);
        em.flush();
        staff.addTeam(team);
        staffRepository.saveAndFlush(staff);
        Long teamId = team.getId();

        // Get all the staffList where team equals to teamId
        defaultStaffShouldBeFound("teamId.equals=" + teamId);

        // Get all the staffList where team equals to (teamId + 1)
        defaultStaffShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaffShouldBeFound(String filter) throws Exception {
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].employeeID").value(hasItem(DEFAULT_EMPLOYEE_ID)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].annualLeaveEntitlement").value(hasItem(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT.intValue())));

        // Check, that the count call also returns 1
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaffShouldNotBeFound(String filter) throws Exception {
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff
        Staff updatedStaff = staffRepository.findById(staff.getId()).get();
        // Disconnect from session so that the updates on updatedStaff are not directly saved in db
        em.detach(updatedStaff);
        updatedStaff
            .position(UPDATED_POSITION)
            .employeeID(UPDATED_EMPLOYEE_ID)
            .startDate(UPDATED_START_DATE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .gender(UPDATED_GENDER)
            .annualLeaveEntitlement(UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
        StaffDTO staffDTO = staffMapper.toDto(updatedStaff);

        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testStaff.getEmployeeID()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testStaff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStaff.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStaff.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStaff.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testStaff.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStaff.getAnnualLeaveEntitlement()).isEqualTo(UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void putNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staffDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaffWithPatch() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff using partial update
        Staff partialUpdatedStaff = new Staff();
        partialUpdatedStaff.setId(staff.getId());

        partialUpdatedStaff.position(UPDATED_POSITION).startDate(UPDATED_START_DATE);

        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaff.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaff))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testStaff.getEmployeeID()).isEqualTo(DEFAULT_EMPLOYEE_ID);
        assertThat(testStaff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaff.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStaff.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStaff.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStaff.getContractNumber()).isEqualTo(DEFAULT_CONTRACT_NUMBER);
        assertThat(testStaff.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testStaff.getAnnualLeaveEntitlement()).isEqualTo(DEFAULT_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void fullUpdateStaffWithPatch() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff using partial update
        Staff partialUpdatedStaff = new Staff();
        partialUpdatedStaff.setId(staff.getId());

        partialUpdatedStaff
            .position(UPDATED_POSITION)
            .employeeID(UPDATED_EMPLOYEE_ID)
            .startDate(UPDATED_START_DATE)
            .name(UPDATED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .gender(UPDATED_GENDER)
            .annualLeaveEntitlement(UPDATED_ANNUAL_LEAVE_ENTITLEMENT);

        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaff.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaff))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testStaff.getEmployeeID()).isEqualTo(UPDATED_EMPLOYEE_ID);
        assertThat(testStaff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStaff.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStaff.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStaff.getContractNumber()).isEqualTo(UPDATED_CONTRACT_NUMBER);
        assertThat(testStaff.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStaff.getAnnualLeaveEntitlement()).isEqualTo(UPDATED_ANNUAL_LEAVE_ENTITLEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staffDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Delete the staff
        restStaffMockMvc
            .perform(delete(ENTITY_API_URL_ID, staff.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
