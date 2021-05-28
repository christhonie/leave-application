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
import za.co.dearx.leave.domain.Decision;
import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.domain.User;
import za.co.dearx.leave.domain.enumeration.DecisionChoice;
import za.co.dearx.leave.repository.DecisionRepository;
import za.co.dearx.leave.service.criteria.DecisionCriteria;
import za.co.dearx.leave.service.dto.DecisionDTO;
import za.co.dearx.leave.service.mapper.DecisionMapper;

/**
 * Integration tests for the {@link DecisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecisionResourceIT {

    private static final DecisionChoice DEFAULT_CHOICE = DecisionChoice.APPROVE;
    private static final DecisionChoice UPDATED_CHOICE = DecisionChoice.REJECT;

    private static final Instant DEFAULT_DECIDED_ON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECIDED_ON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private DecisionMapper decisionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionMockMvc;

    private Decision decision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createEntity(EntityManager em) {
        Decision decision = new Decision().choice(DEFAULT_CHOICE).decidedOn(DEFAULT_DECIDED_ON);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decision.setUser(user);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        decision.setLeaveApplication(leaveApplication);
        return decision;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createUpdatedEntity(EntityManager em) {
        Decision decision = new Decision().choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decision.setUser(user);
        // Add required entity
        LeaveApplication leaveApplication;
        if (TestUtil.findAll(em, LeaveApplication.class).isEmpty()) {
            leaveApplication = LeaveApplicationResourceIT.createUpdatedEntity(em);
            em.persist(leaveApplication);
            em.flush();
        } else {
            leaveApplication = TestUtil.findAll(em, LeaveApplication.class).get(0);
        }
        decision.setLeaveApplication(leaveApplication);
        return decision;
    }

    @BeforeEach
    public void initTest() {
        decision = createEntity(em);
    }

    @Test
    @Transactional
    void createDecision() throws Exception {
        int databaseSizeBeforeCreate = decisionRepository.findAll().size();
        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);
        restDecisionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeCreate + 1);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getChoice()).isEqualTo(DEFAULT_CHOICE);
        assertThat(testDecision.getDecidedOn()).isEqualTo(DEFAULT_DECIDED_ON);
    }

    @Test
    @Transactional
    void createDecisionWithExistingId() throws Exception {
        // Create the Decision with an existing ID
        decision.setId(1L);
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        int databaseSizeBeforeCreate = decisionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChoiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionRepository.findAll().size();
        // set the field null
        decision.setChoice(null);

        // Create the Decision, which fails.
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        restDecisionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecidedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = decisionRepository.findAll().size();
        // set the field null
        decision.setDecidedOn(null);

        // Create the Decision, which fails.
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        restDecisionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecisions() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));
    }

    @Test
    @Transactional
    void getDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get the decision
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL_ID, decision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decision.getId().intValue()))
            .andExpect(jsonPath("$.choice").value(DEFAULT_CHOICE.toString()))
            .andExpect(jsonPath("$.decidedOn").value(DEFAULT_DECIDED_ON.toString()));
    }

    @Test
    @Transactional
    void getDecisionsByIdFiltering() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        Long id = decision.getId();

        defaultDecisionShouldBeFound("id.equals=" + id);
        defaultDecisionShouldNotBeFound("id.notEquals=" + id);

        defaultDecisionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDecisionShouldNotBeFound("id.greaterThan=" + id);

        defaultDecisionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDecisionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where choice equals to DEFAULT_CHOICE
        defaultDecisionShouldBeFound("choice.equals=" + DEFAULT_CHOICE);

        // Get all the decisionList where choice equals to UPDATED_CHOICE
        defaultDecisionShouldNotBeFound("choice.equals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where choice not equals to DEFAULT_CHOICE
        defaultDecisionShouldNotBeFound("choice.notEquals=" + DEFAULT_CHOICE);

        // Get all the decisionList where choice not equals to UPDATED_CHOICE
        defaultDecisionShouldBeFound("choice.notEquals=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsInShouldWork() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where choice in DEFAULT_CHOICE or UPDATED_CHOICE
        defaultDecisionShouldBeFound("choice.in=" + DEFAULT_CHOICE + "," + UPDATED_CHOICE);

        // Get all the decisionList where choice equals to UPDATED_CHOICE
        defaultDecisionShouldNotBeFound("choice.in=" + UPDATED_CHOICE);
    }

    @Test
    @Transactional
    void getAllDecisionsByChoiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where choice is not null
        defaultDecisionShouldBeFound("choice.specified=true");

        // Get all the decisionList where choice is null
        defaultDecisionShouldNotBeFound("choice.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decidedOn equals to DEFAULT_DECIDED_ON
        defaultDecisionShouldBeFound("decidedOn.equals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionShouldNotBeFound("decidedOn.equals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsNotEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decidedOn not equals to DEFAULT_DECIDED_ON
        defaultDecisionShouldNotBeFound("decidedOn.notEquals=" + DEFAULT_DECIDED_ON);

        // Get all the decisionList where decidedOn not equals to UPDATED_DECIDED_ON
        defaultDecisionShouldBeFound("decidedOn.notEquals=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsInShouldWork() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decidedOn in DEFAULT_DECIDED_ON or UPDATED_DECIDED_ON
        defaultDecisionShouldBeFound("decidedOn.in=" + DEFAULT_DECIDED_ON + "," + UPDATED_DECIDED_ON);

        // Get all the decisionList where decidedOn equals to UPDATED_DECIDED_ON
        defaultDecisionShouldNotBeFound("decidedOn.in=" + UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecidedOnIsNullOrNotNull() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decidedOn is not null
        defaultDecisionShouldBeFound("decidedOn.specified=true");

        // Get all the decisionList where decidedOn is null
        defaultDecisionShouldNotBeFound("decidedOn.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);
        Comment comment = CommentResourceIT.createEntity(em);
        em.persist(comment);
        em.flush();
        decision.setComment(comment);
        decisionRepository.saveAndFlush(decision);
        Long commentId = comment.getId();

        // Get all the decisionList where comment equals to commentId
        defaultDecisionShouldBeFound("commentId.equals=" + commentId);

        // Get all the decisionList where comment equals to (commentId + 1)
        defaultDecisionShouldNotBeFound("commentId.equals=" + (commentId + 1));
    }

    @Test
    @Transactional
    void getAllDecisionsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        decision.setUser(user);
        decisionRepository.saveAndFlush(decision);
        Long userId = user.getId();

        // Get all the decisionList where user equals to userId
        defaultDecisionShouldBeFound("userId.equals=" + userId);

        // Get all the decisionList where user equals to (userId + 1)
        defaultDecisionShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllDecisionsByLeaveApplicationIsEqualToSomething() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);
        LeaveApplication leaveApplication = LeaveApplicationResourceIT.createEntity(em);
        em.persist(leaveApplication);
        em.flush();
        decision.setLeaveApplication(leaveApplication);
        decisionRepository.saveAndFlush(decision);
        Long leaveApplicationId = leaveApplication.getId();

        // Get all the decisionList where leaveApplication equals to leaveApplicationId
        defaultDecisionShouldBeFound("leaveApplicationId.equals=" + leaveApplicationId);

        // Get all the decisionList where leaveApplication equals to (leaveApplicationId + 1)
        defaultDecisionShouldNotBeFound("leaveApplicationId.equals=" + (leaveApplicationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionShouldBeFound(String filter) throws Exception {
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].choice").value(hasItem(DEFAULT_CHOICE.toString())))
            .andExpect(jsonPath("$.[*].decidedOn").value(hasItem(DEFAULT_DECIDED_ON.toString())));

        // Check, that the count call also returns 1
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionShouldNotBeFound(String filter) throws Exception {
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecision() throws Exception {
        // Get the decision
        restDecisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision
        Decision updatedDecision = decisionRepository.findById(decision.getId()).get();
        // Disconnect from session so that the updates on updatedDecision are not directly saved in db
        em.detach(updatedDecision);
        updatedDecision.choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);
        DecisionDTO decisionDTO = decisionMapper.toDto(updatedDecision);

        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getChoice()).isEqualTo(UPDATED_CHOICE);
        assertThat(testDecision.getDecidedOn()).isEqualTo(UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void putNonExistingDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getChoice()).isEqualTo(DEFAULT_CHOICE);
        assertThat(testDecision.getDecidedOn()).isEqualTo(DEFAULT_DECIDED_ON);
    }

    @Test
    @Transactional
    void fullUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        partialUpdatedDecision.choice(UPDATED_CHOICE).decidedOn(UPDATED_DECIDED_ON);

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
        Decision testDecision = decisionList.get(decisionList.size() - 1);
        assertThat(testDecision.getChoice()).isEqualTo(UPDATED_CHOICE);
        assertThat(testDecision.getDecidedOn()).isEqualTo(UPDATED_DECIDED_ON);
    }

    @Test
    @Transactional
    void patchNonExistingDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decisionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecision() throws Exception {
        int databaseSizeBeforeUpdate = decisionRepository.findAll().size();
        decision.setId(count.incrementAndGet());

        // Create the Decision
        DecisionDTO decisionDTO = decisionMapper.toDto(decision);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decisionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecision() throws Exception {
        // Initialize the database
        decisionRepository.saveAndFlush(decision);

        int databaseSizeBeforeDelete = decisionRepository.findAll().size();

        // Delete the decision
        restDecisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, decision.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Decision> decisionList = decisionRepository.findAll();
        assertThat(decisionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
