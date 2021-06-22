package za.co.dearx.leave.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import za.co.dearx.leave.domain.PublicHoliday;
import za.co.dearx.leave.repository.PublicHolidayRepository;
import za.co.dearx.leave.service.criteria.PublicHolidayCriteria;
import za.co.dearx.leave.service.dto.PublicHolidayDTO;
import za.co.dearx.leave.service.mapper.PublicHolidayMapper;

/**
 * Integration tests for the {@link PublicHolidayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublicHolidayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/public-holidays";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PublicHolidayRepository publicHolidayRepository;

    @Autowired
    private PublicHolidayMapper publicHolidayMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublicHolidayMockMvc;

    private PublicHoliday publicHoliday;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublicHoliday createEntity(EntityManager em) {
        PublicHoliday publicHoliday = new PublicHoliday().name(DEFAULT_NAME).date(DEFAULT_DATE);
        return publicHoliday;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublicHoliday createUpdatedEntity(EntityManager em) {
        PublicHoliday publicHoliday = new PublicHoliday().name(UPDATED_NAME).date(UPDATED_DATE);
        return publicHoliday;
    }

    @BeforeEach
    public void initTest() {
        publicHoliday = createEntity(em);
    }

    @Test
    @Transactional
    void createPublicHoliday() throws Exception {
        int databaseSizeBeforeCreate = publicHolidayRepository.findAll().size();
        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);
        restPublicHolidayMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeCreate + 1);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublicHoliday.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createPublicHolidayWithExistingId() throws Exception {
        // Create the PublicHoliday with an existing ID
        publicHoliday.setId(1L);
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        int databaseSizeBeforeCreate = publicHolidayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublicHolidayMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPublicHolidays() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getPublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get the publicHoliday
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL_ID, publicHoliday.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publicHoliday.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getPublicHolidaysByIdFiltering() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        Long id = publicHoliday.getId();

        defaultPublicHolidayShouldBeFound("id.equals=" + id);
        defaultPublicHolidayShouldNotBeFound("id.notEquals=" + id);

        defaultPublicHolidayShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPublicHolidayShouldNotBeFound("id.greaterThan=" + id);

        defaultPublicHolidayShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPublicHolidayShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name equals to DEFAULT_NAME
        defaultPublicHolidayShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the publicHolidayList where name equals to UPDATED_NAME
        defaultPublicHolidayShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name not equals to DEFAULT_NAME
        defaultPublicHolidayShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the publicHolidayList where name not equals to UPDATED_NAME
        defaultPublicHolidayShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPublicHolidayShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the publicHolidayList where name equals to UPDATED_NAME
        defaultPublicHolidayShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name is not null
        defaultPublicHolidayShouldBeFound("name.specified=true");

        // Get all the publicHolidayList where name is null
        defaultPublicHolidayShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameContainsSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name contains DEFAULT_NAME
        defaultPublicHolidayShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the publicHolidayList where name contains UPDATED_NAME
        defaultPublicHolidayShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByNameNotContainsSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where name does not contain DEFAULT_NAME
        defaultPublicHolidayShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the publicHolidayList where name does not contain UPDATED_NAME
        defaultPublicHolidayShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date equals to DEFAULT_DATE
        defaultPublicHolidayShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date equals to UPDATED_DATE
        defaultPublicHolidayShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date not equals to DEFAULT_DATE
        defaultPublicHolidayShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date not equals to UPDATED_DATE
        defaultPublicHolidayShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsInShouldWork() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date in DEFAULT_DATE or UPDATED_DATE
        defaultPublicHolidayShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the publicHolidayList where date equals to UPDATED_DATE
        defaultPublicHolidayShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date is not null
        defaultPublicHolidayShouldBeFound("date.specified=true");

        // Get all the publicHolidayList where date is null
        defaultPublicHolidayShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date is greater than or equal to DEFAULT_DATE
        defaultPublicHolidayShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date is greater than or equal to UPDATED_DATE
        defaultPublicHolidayShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date is less than or equal to DEFAULT_DATE
        defaultPublicHolidayShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date is less than or equal to SMALLER_DATE
        defaultPublicHolidayShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date is less than DEFAULT_DATE
        defaultPublicHolidayShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date is less than UPDATED_DATE
        defaultPublicHolidayShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublicHolidaysByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        // Get all the publicHolidayList where date is greater than DEFAULT_DATE
        defaultPublicHolidayShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the publicHolidayList where date is greater than SMALLER_DATE
        defaultPublicHolidayShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPublicHolidayShouldBeFound(String filter) throws Exception {
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publicHoliday.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPublicHolidayShouldNotBeFound(String filter) throws Exception {
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublicHolidayMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPublicHoliday() throws Exception {
        // Get the publicHoliday
        restPublicHolidayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();

        // Update the publicHoliday
        PublicHoliday updatedPublicHoliday = publicHolidayRepository.findById(publicHoliday.getId()).get();
        // Disconnect from session so that the updates on updatedPublicHoliday are not directly saved in db
        em.detach(updatedPublicHoliday);
        updatedPublicHoliday.name(UPDATED_NAME).date(UPDATED_DATE);
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(updatedPublicHoliday);

        restPublicHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publicHolidayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isOk());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublicHoliday.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publicHolidayDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublicHolidayWithPatch() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();

        // Update the publicHoliday using partial update
        PublicHoliday partialUpdatedPublicHoliday = new PublicHoliday();
        partialUpdatedPublicHoliday.setId(publicHoliday.getId());

        partialUpdatedPublicHoliday.date(UPDATED_DATE);

        restPublicHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublicHoliday.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPublicHoliday))
            )
            .andExpect(status().isOk());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublicHoliday.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdatePublicHolidayWithPatch() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();

        // Update the publicHoliday using partial update
        PublicHoliday partialUpdatedPublicHoliday = new PublicHoliday();
        partialUpdatedPublicHoliday.setId(publicHoliday.getId());

        partialUpdatedPublicHoliday.name(UPDATED_NAME).date(UPDATED_DATE);

        restPublicHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublicHoliday.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPublicHoliday))
            )
            .andExpect(status().isOk());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
        PublicHoliday testPublicHoliday = publicHolidayList.get(publicHolidayList.size() - 1);
        assertThat(testPublicHoliday.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublicHoliday.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publicHolidayDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublicHoliday() throws Exception {
        int databaseSizeBeforeUpdate = publicHolidayRepository.findAll().size();
        publicHoliday.setId(count.incrementAndGet());

        // Create the PublicHoliday
        PublicHolidayDTO publicHolidayDTO = publicHolidayMapper.toDto(publicHoliday);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublicHolidayMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(publicHolidayDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublicHoliday in the database
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublicHoliday() throws Exception {
        // Initialize the database
        publicHolidayRepository.saveAndFlush(publicHoliday);

        int databaseSizeBeforeDelete = publicHolidayRepository.findAll().size();

        // Delete the publicHoliday
        restPublicHolidayMockMvc
            .perform(delete(ENTITY_API_URL_ID, publicHoliday.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PublicHoliday> publicHolidayList = publicHolidayRepository.findAll();
        assertThat(publicHolidayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
