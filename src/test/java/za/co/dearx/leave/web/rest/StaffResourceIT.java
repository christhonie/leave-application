package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import za.co.dearx.leave.LeaveApplicationApp;
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.domain.Team;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.repository.StaffRepository;
import za.co.dearx.leave.service.StaffQueryService;
import za.co.dearx.leave.service.StaffService;
import za.co.dearx.leave.service.dto.StaffCriteria;
import za.co.dearx.leave.service.dto.StaffDTO;
import za.co.dearx.leave.service.mapper.StaffMapper;

/**
 * Integration tests for the {@link StaffResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class StaffResourceIT {
    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AA";
    private static final String UPDATED_GENDER = "BB";

    @Autowired
    private StaffRepository staffRepository;

    @Mock
    private StaffRepository staffRepositoryMock;

    @Autowired
    private StaffMapper staffMapper;

    @Mock
    private StaffService staffServiceMock;

    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffQueryService staffQueryService;

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
            .name(DEFAULT_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .gender(DEFAULT_GENDER);
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
            .name(UPDATED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .gender(UPDATED_GENDER);
        return staff;
    }

    @BeforeEach
    public void initTest() {
        staff = createEntity(em);
    }

    @Test
    @Transactional
    public void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();
        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);
        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
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
    }

    @Test
    @Transactional
    public void createStaffWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // Create the Staff with an existing ID
        staff.setId(1L);
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEmployeeIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setEmployeeID(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setStartDate(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setFirstName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setLastName(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setGender(null);

        // Create the Staff, which fails.
        StaffDTO staffDTO = staffMapper.toDto(staff);

        restStaffMockMvc
            .perform(
                post("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList
        restStaffMockMvc
            .perform(get("/api/staff?sort=id,desc"))
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
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllStaffWithEagerRelationshipsIsEnabled() throws Exception {
        when(staffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffMockMvc.perform(get("/api/staff?eagerload=true")).andExpect(status().isOk());

        verify(staffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    public void getAllStaffWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(staffServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStaffMockMvc.perform(get("/api/staff?eagerload=true")).andExpect(status().isOk());

        verify(staffServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc
            .perform(get("/api/staff/{id}", staff.getId()))
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
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    public void getStaffByIdFiltering() throws Exception {
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
    public void getAllStaffByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position equals to DEFAULT_POSITION
        defaultStaffShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the staffList where position equals to UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllStaffByPositionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position not equals to DEFAULT_POSITION
        defaultStaffShouldNotBeFound("position.notEquals=" + DEFAULT_POSITION);

        // Get all the staffList where position not equals to UPDATED_POSITION
        defaultStaffShouldBeFound("position.notEquals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllStaffByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultStaffShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the staffList where position equals to UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllStaffByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position is not null
        defaultStaffShouldBeFound("position.specified=true");

        // Get all the staffList where position is null
        defaultStaffShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByPositionContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position contains DEFAULT_POSITION
        defaultStaffShouldBeFound("position.contains=" + DEFAULT_POSITION);

        // Get all the staffList where position contains UPDATED_POSITION
        defaultStaffShouldNotBeFound("position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllStaffByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where position does not contain DEFAULT_POSITION
        defaultStaffShouldNotBeFound("position.doesNotContain=" + DEFAULT_POSITION);

        // Get all the staffList where position does not contain UPDATED_POSITION
        defaultStaffShouldBeFound("position.doesNotContain=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID equals to DEFAULT_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.equals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.equals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID not equals to DEFAULT_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.notEquals=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID not equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.notEquals=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID in DEFAULT_EMPLOYEE_ID or UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.in=" + DEFAULT_EMPLOYEE_ID + "," + UPDATED_EMPLOYEE_ID);

        // Get all the staffList where employeeID equals to UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.in=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID is not null
        defaultStaffShouldBeFound("employeeID.specified=true");

        // Get all the staffList where employeeID is null
        defaultStaffShouldNotBeFound("employeeID.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID contains DEFAULT_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.contains=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID contains UPDATED_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.contains=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllStaffByEmployeeIDNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where employeeID does not contain DEFAULT_EMPLOYEE_ID
        defaultStaffShouldNotBeFound("employeeID.doesNotContain=" + DEFAULT_EMPLOYEE_ID);

        // Get all the staffList where employeeID does not contain UPDATED_EMPLOYEE_ID
        defaultStaffShouldBeFound("employeeID.doesNotContain=" + UPDATED_EMPLOYEE_ID);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate equals to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate equals to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate not equals to DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate not equals to UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the staffList where startDate equals to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is not null
        defaultStaffShouldBeFound("startDate.specified=true");

        // Get all the staffList where startDate is null
        defaultStaffShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is greater than or equal to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.greaterThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is greater than or equal to UPDATED_START_DATE
        defaultStaffShouldNotBeFound("startDate.greaterThanOrEqual=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is less than or equal to DEFAULT_START_DATE
        defaultStaffShouldBeFound("startDate.lessThanOrEqual=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is less than or equal to SMALLER_START_DATE
        defaultStaffShouldNotBeFound("startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is less than DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is less than UPDATED_START_DATE
        defaultStaffShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where startDate is greater than DEFAULT_START_DATE
        defaultStaffShouldNotBeFound("startDate.greaterThan=" + DEFAULT_START_DATE);

        // Get all the staffList where startDate is greater than SMALLER_START_DATE
        defaultStaffShouldBeFound("startDate.greaterThan=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    public void getAllStaffByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name equals to DEFAULT_NAME
        defaultStaffShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the staffList where name equals to UPDATED_NAME
        defaultStaffShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name not equals to DEFAULT_NAME
        defaultStaffShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the staffList where name not equals to UPDATED_NAME
        defaultStaffShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByNameIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStaffShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the staffList where name equals to UPDATED_NAME
        defaultStaffShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name is not null
        defaultStaffShouldBeFound("name.specified=true");

        // Get all the staffList where name is null
        defaultStaffShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByNameContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name contains DEFAULT_NAME
        defaultStaffShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the staffList where name contains UPDATED_NAME
        defaultStaffShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByNameNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where name does not contain DEFAULT_NAME
        defaultStaffShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the staffList where name does not contain UPDATED_NAME
        defaultStaffShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
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
    public void getAllStaffByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName not equals to DEFAULT_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName not equals to UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the staffList where firstName equals to UPDATED_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName is not null
        defaultStaffShouldBeFound("firstName.specified=true");

        // Get all the staffList where firstName is null
        defaultStaffShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName contains DEFAULT_FIRST_NAME
        defaultStaffShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName contains UPDATED_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where firstName does not contain DEFAULT_FIRST_NAME
        defaultStaffShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the staffList where firstName does not contain UPDATED_FIRST_NAME
        defaultStaffShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName equals to DEFAULT_LAST_NAME
        defaultStaffShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName equals to UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName not equals to DEFAULT_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName not equals to UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the staffList where lastName equals to UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName is not null
        defaultStaffShouldBeFound("lastName.specified=true");

        // Get all the staffList where lastName is null
        defaultStaffShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName contains DEFAULT_LAST_NAME
        defaultStaffShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName contains UPDATED_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where lastName does not contain DEFAULT_LAST_NAME
        defaultStaffShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the staffList where lastName does not contain UPDATED_LAST_NAME
        defaultStaffShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStaffByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email equals to DEFAULT_EMAIL
        defaultStaffShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the staffList where email equals to UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStaffByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email not equals to DEFAULT_EMAIL
        defaultStaffShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the staffList where email not equals to UPDATED_EMAIL
        defaultStaffShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStaffByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStaffShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the staffList where email equals to UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStaffByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email is not null
        defaultStaffShouldBeFound("email.specified=true");

        // Get all the staffList where email is null
        defaultStaffShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByEmailContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email contains DEFAULT_EMAIL
        defaultStaffShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the staffList where email contains UPDATED_EMAIL
        defaultStaffShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStaffByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where email does not contain DEFAULT_EMAIL
        defaultStaffShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the staffList where email does not contain UPDATED_EMAIL
        defaultStaffShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber equals to DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber not equals to DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.notEquals=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber not equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.notEquals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber in DEFAULT_CONTRACT_NUMBER or UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber equals to UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.in=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber is not null
        defaultStaffShouldBeFound("contractNumber.specified=true");

        // Get all the staffList where contractNumber is null
        defaultStaffShouldNotBeFound("contractNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber contains DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber contains UPDATED_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.contains=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStaffByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where contractNumber does not contain DEFAULT_CONTRACT_NUMBER
        defaultStaffShouldNotBeFound("contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER);

        // Get all the staffList where contractNumber does not contain UPDATED_CONTRACT_NUMBER
        defaultStaffShouldBeFound("contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStaffByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender equals to DEFAULT_GENDER
        defaultStaffShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the staffList where gender equals to UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStaffByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender not equals to DEFAULT_GENDER
        defaultStaffShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the staffList where gender not equals to UPDATED_GENDER
        defaultStaffShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStaffByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultStaffShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the staffList where gender equals to UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStaffByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender is not null
        defaultStaffShouldBeFound("gender.specified=true");

        // Get all the staffList where gender is null
        defaultStaffShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllStaffByGenderContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender contains DEFAULT_GENDER
        defaultStaffShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the staffList where gender contains UPDATED_GENDER
        defaultStaffShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStaffByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList where gender does not contain DEFAULT_GENDER
        defaultStaffShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the staffList where gender does not contain UPDATED_GENDER
        defaultStaffShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStaffByUserIsEqualToSomething() throws Exception {
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

        // Get all the staffList where user equals to userId + 1
        defaultStaffShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    public void getAllStaffByTeamIsEqualToSomething() throws Exception {
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

        // Get all the staffList where team equals to teamId + 1
        defaultStaffShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStaffShouldBeFound(String filter) throws Exception {
        restStaffMockMvc
            .perform(get("/api/staff?sort=id,desc&" + filter))
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
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));

        // Check, that the count call also returns 1
        restStaffMockMvc
            .perform(get("/api/staff/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStaffShouldNotBeFound(String filter) throws Exception {
        restStaffMockMvc
            .perform(get("/api/staff?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStaffMockMvc
            .perform(get("/api/staff/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get("/api/staff/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaff() throws Exception {
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
            .name(UPDATED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .gender(UPDATED_GENDER);
        StaffDTO staffDTO = staffMapper.toDto(updatedStaff);

        restStaffMockMvc
            .perform(
                put("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
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
    }

    @Test
    @Transactional
    public void updateNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Create the Staff
        StaffDTO staffDTO = staffMapper.toDto(staff);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put("/api/staff").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staffDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Delete the staff
        restStaffMockMvc
            .perform(delete("/api/staff/{id}", staff.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
