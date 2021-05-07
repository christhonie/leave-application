package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import za.co.dearx.leave.domain.Comment;
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;
import za.co.dearx.leave.repository.DecisionsRepository;
import za.co.dearx.leave.service.DecisionsQueryService;
import za.co.dearx.leave.service.DecisionsService;
import za.co.dearx.leave.service.dto.DecisionsCriteria;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.service.mapper.DecisionsMapper;

/**
 * Integration tests for the {@link DecisionsResource} REST controller.
 */
@SpringBootTest(classes = LeaveApplicationApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DecisionsResourceIT {
    private static final DecisionChoice DEFAULT_CHOICE = DecisionChoice.APPROVE;
    private static final DecisionChoice UPDATED_CHOICE = DecisionChoice.REJECT;

    private static final Instant DEFAULT_DECIDED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECIDED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DecisionsRepository decisionsRepository;

    @Autowired
    private DecisionsMapper decisionsMapper;

    @Autowired
    private DecisionsService decisionsService;

    @Autowired
    private DecisionsQueryService decisionsQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionsMockMvc;

    private Decisions decisions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decisions createEntity(EntityManager em) {
        Decisions decisions = new Decisions().choice(DEFAULT_CHOICE).decidedOn(DEFAULT_DECIDED_ON);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decisions.setUser(user);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        decisions.setLeaveApplication(leaveApplication);
        return decisions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decisions createUpdatedEntity(EntityManager em) {
        Decisions decisions = new Decisions().choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decisions.setUser(user);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createUpdatedEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        decisions.setLeaveApplication(leaveApplication);
        return decisions;
    }

    @BeforeEach
    public void initTest() {
        decisions = createEntity(em);
    }

    @Test
    @Transactional
    public void createDecisions() throws Exception {
        int databaseSizeBeforeCreate = decisionsRepository.findAll().size();
        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);
        restDecisionsMockMvc
            .perform(
                post("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeCreate + 1);
        Decisions testDecisions = decisionsList.get(decisionsList.size() - 1);
        assertThat(testDecisions.getChoice()).isEqualTo(DEFAULT_CHOICE);
        assertThat(testDecisions.getDecidedOn()).isEqualTo(DEFAULT_DECIDED_ON);
    }

    @Test
    @Transactional
    public void createDecisionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = decisionsRepository.findAll().size();

        // Create the Decisions with an existing ID
        decisions.setId(1L);
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionsMockMvc
            .perform(
                post("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionsRepository.findAll().size();
        // set the field null
        decisions.setChoice(null);

        // Create the Decisions, which fails.
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        restDecisionsMockMvc
            .perform(
                post("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDecidedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionsRepository.findAll().size();
        // set the field null
        decisions.setDecidedOn(null);

        // Create the Decisions, which fails.
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        restDecisionsMockMvc
            .perform(
                post("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList
        restDecisionsMockMvc
            .perform(get("/api/decisions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisions.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));
    }

    @Test
    @Transactional
    public void getDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get the decisions
        restDecisionsMockMvc
            .perform(get("/api/decisions/{id}", decisions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decisions.getId().intValue()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE.toString()))
            .andExpect(jsonPath("$.decidedOn").value(DEFAULT_DECIDED_ON.toString()));
    }

    @Test
    @Transactional
    public void getDecisionsByIdFiltering() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        Long id = decisions.getId();

        defaultDecisionsShouldBeFound("id.equals=" + id);
        defaultDecisionsShouldNotBeFound("id.notEquals=" + id);

        defaultDecisionsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDecisionsShouldNotBeFound("id.greaterThan=" + id);

        defaultDecisionsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDecisionsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllDecisionsByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice equals to DEFAULT_CHOICE
        defaultDecisionsShouldBeFound("choice.equals=" + DEFAULT_CHOICE);

        // Get all the decisionsList where choice equals to UPDATED_CHOICE
        defaultDecisionsShouldNotBeFound("choice.equals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllDecisionsByChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice not equals to DEFAULT_CHOICE
        defaultDecisionsShouldNotBeFound("choice.notEquals=" + DEFAULT_CHOICE);

        // Get all the decisionsList where choice not equals to UPDATED_CHOICE
        defaultDecisionsShouldBeFound("choice.notEquals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllDecisionsByChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice in DEFAULT_CHOICE or UPDATED_CHOICE
        defaultDecisionsShouldBeFound("choice.in=" + DEFAULT_CHOICE + "," + UPDATED_CHOICE);

        // Get all the decisionsList where choice equals to UPDATED_CHOICE
        defaultDecisionsShouldNotBeFound("choice.in=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    public void getAllDecisionsByChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice is not null
        defaultDecisionsShouldBeFound("choice.specified=true");

        // Get all the decisionsList where choice is null
        defaultDecisionsShouldNotBeFound("choice.specified=false");
    }

    @Test
    @Transactional
    public void getAllDecisionsByDecidedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn equals to DEFAULT_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.equals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionsList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.equals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    public void getAllDecisionsByDecidedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn not equals to DEFAULT_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.notEquals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionsList where decidedOn not equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.notEquals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    public void getAllDecisionsByDecidedOnIsInShouldWork() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn in DEFAULT_DECIDED_ON or UPDATED_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.in=" + DEFAULT_DECIDED_ON + "," + UPDATED_DECIDED_ON);

        // Get all the decisionsList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.in=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    public void getAllDecisionsByDecidedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn is not null
        defaultDecisionsShouldBeFound("decidedOn.specified=true");

        // Get all the decisionsList where decidedOn is null
        defaultDecisionsShouldNotBeFound("decidedOn.specified=false");
    }

    @Test
    @Transactional
    public void getAllDecisionsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);
        Comment comment = CommentResourceIT.createEntity(em);
        em.persist(comment);
        em.flush();
        decisions.setComment(comment);
        decisionsRepository.saveAndFlush(decisions);
        Long commentId = comment.getId();

        // Get all the decisionsList where comment equals to commentId
        defaultDecisionsShouldBeFound("commentId.equals=" + commentId);

        // Get all the decisionsList where comment equals to commentId + 1
        defaultDecisionsShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    public void getAllDecisionsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = decisions.getUser();
        decisionsRepository.saveAndFlush(decisions);
        Long userId = user.getId();

        // Get all the decisionsList where user equals to userId
        defaultDecisionsShouldBeFound("userId.equals=" + userId);

        // Get all the decisionsList where user equals to userId + 1
        defaultDecisionsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    public void getAllDecisionsByLeaveApplicationIsEqualToSomething() throws Exception {
        // Get already existing entity
        LeaveApplication leaveApplication = decisions.getLeaveApplication();
        decisionsRepository.saveAndFlush(decisions);
        Long leaveApplicationId = leaveApplication.getId();

        // Get all the decisionsList where leaveApplication equals to leaveApplicationId
        defaultDecisionsShouldBeFound("leaveApplicationId.equals=" + leaveApplicationId);

        // Get all the decisionsList where leaveApplication equals to leaveApplicationId + 1
        defaultDecisionsShouldNotBeFound("leaveApplicationId.equals=" + (leaveApplicationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionsShouldBeFound(String filter) throws Exception {
        restDecisionsMockMvc
            .perform(get("/api/decisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisions.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));

        // Check, that the count call also returns 1
        restDecisionsMockMvc
            .perform(get("/api/decisions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionsShouldNotBeFound(String filter) throws Exception {
        restDecisionsMockMvc
            .perform(get("/api/decisions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionsMockMvc
            .perform(get("/api/decisions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDecisions() throws Exception {
        // Get the decisions
        restDecisionsMockMvc.perform(get("/api/decisions/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();

        // Update the decisions
        Decisions updatedDecisions = decisionsRepository.findById(decisions.getId()).get();
        // Disconnect from session so that the updates on updatedDecisions are not directly saved in db
        em.detach(updatedDecisions);
        updatedDecisions.choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(updatedDecisions);

        restDecisionsMockMvc
            .perform(
                put("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
        Decisions testDecisions = decisionsList.get(decisionsList.size() - 1);
        assertThat(testDecisions.getChoice()).isEqualTo(UPDATED_CHOICE);
        assertThat(testDecisions.getDecidedOn()).isEqualTo(UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    public void updateNonExistingDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                put("/api/decisions")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        int databaseSizeBeforeDelete = decisionsRepository.findAll().size();

        // Delete the decisions
        restDecisionsMockMvc
            .perform(delete("/api/decisions/{id}", decisions.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
