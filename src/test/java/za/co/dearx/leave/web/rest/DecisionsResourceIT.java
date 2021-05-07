package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import za.co.dearx.leave.domain.Comment;
import za.co.dearx.leave.domain.Decisions;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;
import za.co.dearx.leave.repository.DecisionsRepository;
import za.co.dearx.leave.service.criteria.DecisionsCriteria;
import za.co.dearx.leave.service.dto.DecisionsDTO;
import za.co.dearx.leave.service.mapper.DecisionsMapper;

/**
 * Integration tests for the {@link DecisionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecisionsResourceIT {

    private static final DecisionChoice DEFAULT_CHOICE = DecisionChoice.APPROVE;
    private static final DecisionChoice UPDATED_CHOICE = DecisionChoice.REJECT;

    private static final Instant DEFAULT_DECIDED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECIDED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DecisionsRepository decisionsRepository;

    @Autowired
    private DecisionsMapper decisionsMapper;

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
    void createDecisions() throws Exception {
        int databaseSizeBeforeCreate = decisionsRepository.findAll().size();
        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);
        restDecisionsMockMvc
            .perform(
                post(ENTITY_API_URL)
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
    void createDecisionsWithExistingId() throws Exception {
        // Create the Decisions with an existing ID
        decisions.setId(1L);
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        int databaseSizeBeforeCreate = decisionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionsMockMvc
            .perform(
                post(ENTITY_API_URL)
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
    void checkChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionsRepository.findAll().size();
        // set the field null
        decisions.setChoice(null);

        // Create the Decisions, which fails.
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        restDecisionsMockMvc
            .perform(
                post(ENTITY_API_URL)
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
    void checkDecidedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionsRepository.findAll().size();
        // set the field null
        decisions.setDecidedOn(null);

        // Create the Decisions, which fails.
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        restDecisionsMockMvc
            .perform(
                post(ENTITY_API_URL)
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
    void getAllDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisions.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));
    }

    @Test
    @Transactional
    void getDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get the decisions
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL_ID, decisions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decisions.getId().intValue()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE.toString()))
            .andExpect(jsonPath("$.decidedOn").value(DEFAULT_DECIDED_ON.toString()));
    }

    @Test
    @Transactional
    void getDecisionsByIdFiltering() throws Exception {
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
    void getAllDecisionsByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice equals to DEFAULT_CHOICE
        defaultDecisionsShouldBeFound("choice.equals=" + DEFAULT_CHOICE);

        // Get all the decisionsList where choice equals to UPDATED_CHOICE
        defaultDecisionsShouldNotBeFound("choice.equals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice not equals to DEFAULT_CHOICE
        defaultDecisionsShouldNotBeFound("choice.notEquals=" + DEFAULT_CHOICE);

        // Get all the decisionsList where choice not equals to UPDATED_CHOICE
        defaultDecisionsShouldBeFound("choice.notEquals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice in DEFAULT_CHOICE or UPDATED_CHOICE
        defaultDecisionsShouldBeFound("choice.in=" + DEFAULT_CHOICE + "," + UPDATED_CHOICE);

        // Get all the decisionsList where choice equals to UPDATED_CHOICE
        defaultDecisionsShouldNotBeFound("choice.in=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where choice is not null
        defaultDecisionsShouldBeFound("choice.specified=true");

        // Get all the decisionsList where choice is null
        defaultDecisionsShouldNotBeFound("choice.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn equals to DEFAULT_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.equals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionsList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.equals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn not equals to DEFAULT_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.notEquals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionsList where decidedOn not equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.notEquals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsInShouldWork() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn in DEFAULT_DECIDED_ON or UPDATED_DECIDED_ON
        defaultDecisionsShouldBeFound("decidedOn.in=" + DEFAULT_DECIDED_ON + "," + UPDATED_DECIDED_ON);

        // Get all the decisionsList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionsShouldNotBeFound("decidedOn.in=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        // Get all the decisionsList where decidedOn is not null
        defaultDecisionsShouldBeFound("decidedOn.specified=true");

        // Get all the decisionsList where decidedOn is null
        defaultDecisionsShouldNotBeFound("decidedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByCommentIsEqualToSomething() throws Exception {
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

        // Get all the decisionsList where comment equals to (commentId + 1)
        defaultDecisionsShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    void getAllDecisionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decisions.setUser(user);
        decisionsRepository.saveAndFlush(decisions);
        Long userId = user.getId();

        // Get all the decisionsList where user equals to userId
        defaultDecisionsShouldBeFound("userId.equals=" + userId);

        // Get all the decisionsList where user equals to (userId + 1)
        defaultDecisionsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllDecisionsByLeaveApplicationIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);
        LeaveApplication leaveApplication = LeaveApplicationResourceIT.createEntity(em);
        em.persist(leaveApplication);
        em.flush();
        decisions.setLeaveApplication(leaveApplication);
        decisionsRepository.saveAndFlush(decisions);
        Long leaveApplicationId = leaveApplication.getId();

        // Get all the decisionsList where leaveApplication equals to leaveApplicationId
        defaultDecisionsShouldBeFound("leaveApplicationId.equals=" + leaveApplicationId);

        // Get all the decisionsList where leaveApplication equals to (leaveApplicationId + 1)
        defaultDecisionsShouldNotBeFound("leaveApplicationId.equals=" + (leaveApplicationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionsShouldBeFound(String filter) throws Exception {
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisions.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));

        // Check, that the count call also returns 1
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionsShouldNotBeFound(String filter) throws Exception {
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecisions() throws Exception {
        // Get the decisions
        restDecisionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDecisions() throws Exception {
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
                put(ENTITY_API_URL_ID, decisionsDTO.getId())
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
    void putNonExistingDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionsDTO.getId())
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
    void putWithIdMismatchDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
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
    void putWithMissingIdPathParamDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecisionsWithPatch() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();

        // Update the decisions using partial update
        Decisions partialUpdatedDecisions = new Decisions();
        partialUpdatedDecisions.setId(decisions.getId());

        partialUpdatedDecisions.choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);

        restDecisionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecisions))
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
    void fullUpdateDecisionsWithPatch() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();

        // Update the decisions using partial update
        Decisions partialUpdatedDecisions = new Decisions();
        partialUpdatedDecisions.setId(decisions.getId());

        partialUpdatedDecisions.choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);

        restDecisionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisions.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecisions))
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
    void patchNonExistingDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decisionsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecisions() throws Exception {
        int databaseSizeBeforeUpdate = decisionsRepository.findAll().size();
        decisions.setId(count.incrementAndGet());

        // Create the Decisions
        DecisionsDTO decisionsDTO = decisionsMapper.toDto(decisions);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decisions in the database
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecisions() throws Exception {
        // Initialize the database
        decisionsRepository.saveAndFlush(decisions);

        int databaseSizeBeforeDelete = decisionsRepository.findAll().size();

        // Delete the decisions
        restDecisionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, decisions.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Decisions> decisionsList = decisionsRepository.findAll();
        assertThat(decisionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
