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
import za.co.dearx.leave.domain.LeaveStatus;
import za.co.dearx.leave.repository.LeaveStatusRepository;
import za.co.dearx.leave.service.LeaveStatusQueryService;
import za.co.dearx.leave.service.LeaveStatusService;
import za.co.dearx.leave.service.dto.LeaveStatusCriteria;
import za.co.dearx.leave.service.dto.LeaveStatusDTO;
import za.co.dearx.leave.service.mapper.LeaveStatusMapper;

/**
 * Integration tests for the {@link LeaveStatusResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LeaveStatusResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private LeaveStatusRepository leaveStatusRepository;

    @Autowired
    private LeaveStatusMapper leaveStatusMapper;

    @Autowired
    private LeaveStatusService leaveStatusService;

    @Autowired
    private LeaveStatusQueryService leaveStatusQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeaveStatusMockMvc;

    private LeaveStatus leaveStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveStatus createEntity(EntityManager em) {
        LeaveStatus leaveStatus = new LeaveStatus().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return leaveStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeaveStatus createUpdatedEntity(EntityManager em) {
        LeaveStatus leaveStatus = new LeaveStatus().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return leaveStatus;
    }

    @BeforeEach
    public void initTest() {
        leaveStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeaveStatus() throws Exception {
        int databaseSizeBeforeCreate = leaveStatusRepository.findAll().size();
        // Create the LeaveStatus
        LeaveStatusDTO leaveStatusDTO = leaveStatusMapper.toDto(leaveStatus);
        restLeaveStatusMockMvc
            .perform(
                post("/api/leave-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveStatusDTO))
            )
            .andExpect(status().isCreated());

        // Validate the LeaveStatus in the database
        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeCreate + 1);
        LeaveStatus testLeaveStatus = leaveStatusList.get(leaveStatusList.size() - 1);
        assertThat(testLeaveStatus.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLeaveStatus.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLeaveStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaveStatusRepository.findAll().size();

        // Create the LeaveStatus with an existing ID
        leaveStatus.setId(1L);
        LeaveStatusDTO leaveStatusDTO = leaveStatusMapper.toDto(leaveStatus);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaveStatusMockMvc
            .perform(
                post("/api/leave-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveStatus in the database
        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaveStatusRepository.findAll().size();
        // set the field null
        leaveStatus.setName(null);

        // Create the LeaveStatus, which fails.
        LeaveStatusDTO leaveStatusDTO = leaveStatusMapper.toDto(leaveStatus);

        restLeaveStatusMockMvc
            .perform(
                post("/api/leave-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveStatusDTO))
            )
            .andExpect(status().isBadRequest());

        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeaveStatuses() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getLeaveStatus() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get the leaveStatus
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses/{id}", leaveStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(leaveStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getLeaveStatusesByIdFiltering() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        Long id = leaveStatus.getId();

        defaultLeaveStatusShouldBeFound("id.equals=" + id);
        defaultLeaveStatusShouldNotBeFound("id.notEquals=" + id);

        defaultLeaveStatusShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLeaveStatusShouldNotBeFound("id.greaterThan=" + id);

        defaultLeaveStatusShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLeaveStatusShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name equals to DEFAULT_NAME
        defaultLeaveStatusShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the leaveStatusList where name equals to UPDATED_NAME
        defaultLeaveStatusShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name not equals to DEFAULT_NAME
        defaultLeaveStatusShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the leaveStatusList where name not equals to UPDATED_NAME
        defaultLeaveStatusShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLeaveStatusShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the leaveStatusList where name equals to UPDATED_NAME
        defaultLeaveStatusShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name is not null
        defaultLeaveStatusShouldBeFound("name.specified=true");

        // Get all the leaveStatusList where name is null
        defaultLeaveStatusShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameContainsSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name contains DEFAULT_NAME
        defaultLeaveStatusShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the leaveStatusList where name contains UPDATED_NAME
        defaultLeaveStatusShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where name does not contain DEFAULT_NAME
        defaultLeaveStatusShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the leaveStatusList where name does not contain UPDATED_NAME
        defaultLeaveStatusShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description equals to DEFAULT_DESCRIPTION
        defaultLeaveStatusShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the leaveStatusList where description equals to UPDATED_DESCRIPTION
        defaultLeaveStatusShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description not equals to DEFAULT_DESCRIPTION
        defaultLeaveStatusShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the leaveStatusList where description not equals to UPDATED_DESCRIPTION
        defaultLeaveStatusShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLeaveStatusShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the leaveStatusList where description equals to UPDATED_DESCRIPTION
        defaultLeaveStatusShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description is not null
        defaultLeaveStatusShouldBeFound("description.specified=true");

        // Get all the leaveStatusList where description is null
        defaultLeaveStatusShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description contains DEFAULT_DESCRIPTION
        defaultLeaveStatusShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the leaveStatusList where description contains UPDATED_DESCRIPTION
        defaultLeaveStatusShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLeaveStatusesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        // Get all the leaveStatusList where description does not contain DEFAULT_DESCRIPTION
        defaultLeaveStatusShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the leaveStatusList where description does not contain UPDATED_DESCRIPTION
        defaultLeaveStatusShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeaveStatusShouldBeFound(String filter) throws Exception {
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leaveStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeaveStatusShouldNotBeFound(String filter) throws Exception {
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeaveStatusMockMvc
            .perform(get("/api/leave-statuses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingLeaveStatus() throws Exception {
        // Get the leaveStatus
        restLeaveStatusMockMvc.perform(get("/api/leave-statuses/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeaveStatus() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        int databaseSizeBeforeUpdate = leaveStatusRepository.findAll().size();

        // Update the leaveStatus
        LeaveStatus updatedLeaveStatus = leaveStatusRepository.findById(leaveStatus.getId()).get();
        // Disconnect from session so that the updates on updatedLeaveStatus are not directly saved in db
        em.detach(updatedLeaveStatus);
        updatedLeaveStatus.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        LeaveStatusDTO leaveStatusDTO = leaveStatusMapper.toDto(updatedLeaveStatus);

        restLeaveStatusMockMvc
            .perform(
                put("/api/leave-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the LeaveStatus in the database
        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeUpdate);
        LeaveStatus testLeaveStatus = leaveStatusList.get(leaveStatusList.size() - 1);
        assertThat(testLeaveStatus.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLeaveStatus.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingLeaveStatus() throws Exception {
        int databaseSizeBeforeUpdate = leaveStatusRepository.findAll().size();

        // Create the LeaveStatus
        LeaveStatusDTO leaveStatusDTO = leaveStatusMapper.toDto(leaveStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeaveStatusMockMvc
            .perform(
                put("/api/leave-statuses")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(leaveStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LeaveStatus in the database
        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeaveStatus() throws Exception {
        // Initialize the database
        leaveStatusRepository.saveAndFlush(leaveStatus);

        int databaseSizeBeforeDelete = leaveStatusRepository.findAll().size();

        // Delete the leaveStatus
        restLeaveStatusMockMvc
            .perform(delete("/api/leave-statuses/{id}", leaveStatus.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeaveStatus> leaveStatusList = leaveStatusRepository.findAll();
        assertThat(leaveStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
