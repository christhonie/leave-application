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
import za.co.dearx.leave.domain.Staff;
import za.co.dearx.leave.domain.Team;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.repository.TeamRepository;
import za.co.dearx.leave.service.TeamQueryService;
import za.co.dearx.leave.service.TeamService;
import za.co.dearx.leave.service.dto.TeamCriteria;
import za.co.dearx.leave.service.dto.TeamDTO;
import za.co.dearx.leave.service.mapper.TeamMapper;

/**
 * Integration tests for the {@link TeamResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TeamResourceIT {
    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamQueryService teamQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeamMockMvc;

    private Team team;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createEntity(EntityManager em) {
        Team team = new Team().name(DEFAULT_NAME);
        return team;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Team createUpdatedEntity(EntityManager em) {
        Team team = new Team().name(UPDATED_NAME);
        return team;
    }

    @BeforeEach
    public void initTest() {
        team = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeam() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();
        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);
        restTeamMockMvc
            .perform(
                post("/api/teams").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate + 1);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createTeamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teamRepository.findAll().size();

        // Create the Team with an existing ID
        team.setId(1L);
        TeamDTO teamDTO = teamMapper.toDto(team);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeamMockMvc
            .perform(
                post("/api/teams").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teamRepository.findAll().size();
        // set the field null
        team.setName(null);

        // Create the Team, which fails.
        TeamDTO teamDTO = teamMapper.toDto(team);

        restTeamMockMvc
            .perform(
                post("/api/teams").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDTO))
            )
            .andExpect(status().isBadRequest());

        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeams() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList
        restTeamMockMvc
            .perform(get("/api/teams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void getTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get the team
        restTeamMockMvc
            .perform(get("/api/teams/{id}", team.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(team.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getTeamsByIdFiltering() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        Long id = team.getId();

        defaultTeamShouldBeFound("id.equals=" + id);
        defaultTeamShouldNotBeFound("id.notEquals=" + id);

        defaultTeamShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTeamShouldNotBeFound("id.greaterThan=" + id);

        defaultTeamShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTeamShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllTeamsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name equals to DEFAULT_NAME
        defaultTeamShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the teamList where name equals to UPDATED_NAME
        defaultTeamShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name not equals to DEFAULT_NAME
        defaultTeamShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the teamList where name not equals to UPDATED_NAME
        defaultTeamShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTeamShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the teamList where name equals to UPDATED_NAME
        defaultTeamShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name is not null
        defaultTeamShouldBeFound("name.specified=true");

        // Get all the teamList where name is null
        defaultTeamShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllTeamsByNameContainsSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name contains DEFAULT_NAME
        defaultTeamShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the teamList where name contains UPDATED_NAME
        defaultTeamShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        // Get all the teamList where name does not contain DEFAULT_NAME
        defaultTeamShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the teamList where name does not contain UPDATED_NAME
        defaultTeamShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTeamsByManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        User manager = UserResourceIT.createEntity(em);
        em.persist(manager);
        em.flush();
        team.setManager(manager);
        teamRepository.saveAndFlush(team);
        Long managerId = manager.getId();

        // Get all the teamList where manager equals to managerId
        defaultTeamShouldBeFound("managerId.equals=" + managerId);

        // Get all the teamList where manager equals to managerId + 1
        defaultTeamShouldNotBeFound("managerId.equals=" + (managerId + 1));
    }

    @Test
    @Transactional
    public void getAllTeamsByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);
        Staff member = StaffResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        team.addMember(member);
        teamRepository.saveAndFlush(team);
        Long memberId = member.getId();

        // Get all the teamList where member equals to memberId
        defaultTeamShouldBeFound("memberId.equals=" + memberId);

        // Get all the teamList where member equals to memberId + 1
        defaultTeamShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTeamShouldBeFound(String filter) throws Exception {
        restTeamMockMvc
            .perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(team.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTeamMockMvc
            .perform(get("/api/teams/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTeamShouldNotBeFound(String filter) throws Exception {
        restTeamMockMvc
            .perform(get("/api/teams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTeamMockMvc
            .perform(get("/api/teams/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingTeam() throws Exception {
        // Get the team
        restTeamMockMvc.perform(get("/api/teams/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Update the team
        Team updatedTeam = teamRepository.findById(team.getId()).get();
        // Disconnect from session so that the updates on updatedTeam are not directly saved in db
        em.detach(updatedTeam);
        updatedTeam.name(UPDATED_NAME);
        TeamDTO teamDTO = teamMapper.toDto(updatedTeam);

        restTeamMockMvc
            .perform(
                put("/api/teams").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDTO))
            )
            .andExpect(status().isOk());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
        Team testTeam = teamList.get(teamList.size() - 1);
        assertThat(testTeam.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingTeam() throws Exception {
        int databaseSizeBeforeUpdate = teamRepository.findAll().size();

        // Create the Team
        TeamDTO teamDTO = teamMapper.toDto(team);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeamMockMvc
            .perform(
                put("/api/teams").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(teamDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Team in the database
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeam() throws Exception {
        // Initialize the database
        teamRepository.saveAndFlush(team);

        int databaseSizeBeforeDelete = teamRepository.findAll().size();

        // Delete the team
        restTeamMockMvc
            .perform(delete("/api/teams/{id}", team.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Team> teamList = teamRepository.findAll();
        assertThat(teamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
