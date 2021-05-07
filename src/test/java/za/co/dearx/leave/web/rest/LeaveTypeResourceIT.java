package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import za.co.dearx.leave.domain.LeaveType;
import za.co.dearx.leave.repository.LeaveTypeRepository;
import za.co.dearx.leave.service.LeaveTypeQueryService;
import za.co.dearx.leave.service.LeaveTypeService;
import za.co.dearx.leave.service.dto.LeaveTypeCriteria;
import za.co.dearx.leave.service.dto.LeaveTypeDTO;
import za.co.dearx.leave.service.mapper.LeaveTypeMapper;

/**
 * Integration tests for the {@link LeaveTypeResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeaveTypeResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROCESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROCESS_NAME = "BBBBBBBBBB";

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    @Autowired
    private LeaveTypeService leaveTypeService;

    @Autowired
    private LeaveTypeQueryService leaveTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveTypeMockMvc;

    private LeaveType leaveType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveType createEntity(EntityManager em) {
        LeaveType leaveType = new LeaveType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).processName(DEFAULT_PROCESS_NAME);
        return leaveType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveType createUpdatedEntity(EntityManager em) {
        LeaveType leaveType = new LeaveType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).processName(UPDATED_PROCESS_NAME);
        return leaveType;
    }

    @BeforeEach
    public void initTest() {
        leaveType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveType() throws Exception {
        int databaseSizeBeforeCreate = leaveTypeRepository.findAll().size();
        // Create the LeaveType
        LeaveTypeDTO leaveTypeDTO = leaveTypeMapper.toDto(leaveType);
        restLeaveTypeMockMvc
            .perform(
                post("/api/leave-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveType in the database
        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveType testLeaveType = leaveTypeList.get(leaveTypeList.size() - 1);
        assertThat(testLeaveType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeaveType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLeaveType.getProcessName()).isEqualTo(DEFAULT_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void createLeaveTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveTypeRepository.findAll().size();

        // Create the LeaveType with an existing ID
        leaveType.setId(1L);
        LeaveTypeDTO leaveTypeDTO = leaveTypeMapper.toDto(leaveType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveTypeMockMvc
            .perform(
                post("/api/leave-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveType in the database
        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveTypeRepository.findAll().size();
        // set the field null
        leaveType.setName(null);

        // Create the LeaveType, which fails.
        LeaveTypeDTO leaveTypeDTO = leaveTypeMapper.toDto(leaveType);

        restLeaveTypeMockMvc
            .perform(
                post("/api/leave-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaveTypes() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME)));
    }

    @Test
    @Transactional
    public void getLeaveType() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get the leaveType
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types/{id}", leaveType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.processName").value(DEFAULT_PROCESS_NAME));
    }

    @Test
    @Transactional
    public void getLeaveTypesByIdFiltering() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        Long id = leaveType.getId();

        defaultLeaveTypeShouldBeFound("id.equals=" + id);
        defaultLeaveTypeShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name equals to DEFAULT_NAME
        defaultLeaveTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the leaveTypeList where name equals to UPDATED_NAME
        defaultLeaveTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name not equals to DEFAULT_NAME
        defaultLeaveTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the leaveTypeList where name not equals to UPDATED_NAME
        defaultLeaveTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLeaveTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the leaveTypeList where name equals to UPDATED_NAME
        defaultLeaveTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name is not null
        defaultLeaveTypeShouldBeFound("name.specified=true");

        // Get all the leaveTypeList where name is null
        defaultLeaveTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name contains DEFAULT_NAME
        defaultLeaveTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the leaveTypeList where name contains UPDATED_NAME
        defaultLeaveTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where name does not contain DEFAULT_NAME
        defaultLeaveTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the leaveTypeList where name does not contain UPDATED_NAME
        defaultLeaveTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description equals to DEFAULT_DESCRIPTION
        defaultLeaveTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leaveTypeList where description equals to UPDATED_DESCRIPTION
        defaultLeaveTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultLeaveTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leaveTypeList where description not equals to UPDATED_DESCRIPTION
        defaultLeaveTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeaveTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leaveTypeList where description equals to UPDATED_DESCRIPTION
        defaultLeaveTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description is not null
        defaultLeaveTypeShouldBeFound("description.specified=true");

        // Get all the leaveTypeList where description is null
        defaultLeaveTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description contains DEFAULT_DESCRIPTION
        defaultLeaveTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leaveTypeList where description contains UPDATED_DESCRIPTION
        defaultLeaveTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultLeaveTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leaveTypeList where description does not contain UPDATED_DESCRIPTION
        defaultLeaveTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName equals to DEFAULT_PROCESS_NAME
        defaultLeaveTypeShouldBeFound("processName.equals=" + DEFAULT_PROCESS_NAME);

        // Get all the leaveTypeList where processName equals to UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldNotBeFound("processName.equals=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName not equals to DEFAULT_PROCESS_NAME
        defaultLeaveTypeShouldNotBeFound("processName.notEquals=" + DEFAULT_PROCESS_NAME);

        // Get all the leaveTypeList where processName not equals to UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldBeFound("processName.notEquals=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameIsInShouldWork() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName in DEFAULT_PROCESS_NAME or UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldBeFound("processName.in=" + DEFAULT_PROCESS_NAME + "," + UPDATED_PROCESS_NAME);

        // Get all the leaveTypeList where processName equals to UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldNotBeFound("processName.in=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName is not null
        defaultLeaveTypeShouldBeFound("processName.specified=true");

        // Get all the leaveTypeList where processName is null
        defaultLeaveTypeShouldNotBeFound("processName.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName contains DEFAULT_PROCESS_NAME
        defaultLeaveTypeShouldBeFound("processName.contains=" + DEFAULT_PROCESS_NAME);

        // Get all the leaveTypeList where processName contains UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldNotBeFound("processName.contains=" + UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveTypesByProcessNameNotContainsSomething() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        // Get all the leaveTypeList where processName does not contain DEFAULT_PROCESS_NAME
        defaultLeaveTypeShouldNotBeFound("processName.doesNotContain=" + DEFAULT_PROCESS_NAME);

        // Get all the leaveTypeList where processName does not contain UPDATED_PROCESS_NAME
        defaultLeaveTypeShouldBeFound("processName.doesNotContain=" + UPDATED_PROCESS_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveTypeShouldBeFound(String filter) throws Exception {
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].processName").value(hasItem(DEFAULT_PROCESS_NAME)));

        // Check, that the count call also returns 1
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveTypeShouldNotBeFound(String filter) throws Exception {
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveTypeMockMvc
            .perform(get("/api/leave-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeaveType() throws Exception {
        // Get the leaveType
        restLeaveTypeMockMvc.perform(get("/api/leave-types/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveType() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        int databaseSizeBeforeUpdate = leaveTypeRepository.findAll().size();

        // Update the leaveType
        LeaveType updatedLeaveType = leaveTypeRepository.findById(leaveType.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveType are not directly saved in db
        em.detach(updatedLeaveType);
        updatedLeaveType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).processName(UPDATED_PROCESS_NAME);
        LeaveTypeDTO leaveTypeDTO = leaveTypeMapper.toDto(updatedLeaveType);

        restLeaveTypeMockMvc
            .perform(
                put("/api/leave-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveType in the database
        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeUpdate);
        LeaveType testLeaveType = leaveTypeList.get(leaveTypeList.size() - 1);
        assertThat(testLeaveType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeaveType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLeaveType.getProcessName()).isEqualTo(UPDATED_PROCESS_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveType() throws Exception {
        int databaseSizeBeforeUpdate = leaveTypeRepository.findAll().size();

        // Create the LeaveType
        LeaveTypeDTO leaveTypeDTO = leaveTypeMapper.toDto(leaveType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveTypeMockMvc
            .perform(
                put("/api/leave-types")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveType in the database
        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveType() throws Exception {
        // Initialize the database
        leaveTypeRepository.saveAndFlush(leaveType);

        int databaseSizeBeforeDelete = leaveTypeRepository.findAll().size();

        // Delete the leaveType
        restLeaveTypeMockMvc
            .perform(delete("/api/leave-types/{id}", leaveType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveType> leaveTypeList = leaveTypeRepository.findAll();
        assertThat(leaveTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
