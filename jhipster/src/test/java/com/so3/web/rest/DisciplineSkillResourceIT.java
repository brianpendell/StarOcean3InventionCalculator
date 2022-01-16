package com.so3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.so3.IntegrationTest;
import com.so3.domain.DisciplineSkill;
import com.so3.repository.DisciplineSkillRepository;
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

/**
 * Integration tests for the {@link DisciplineSkillResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisciplineSkillResourceIT {

    private static final Integer DEFAULT_SKILL_MODIFIER = 1;
    private static final Integer UPDATED_SKILL_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/discipline-skills";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisciplineSkillRepository disciplineSkillRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisciplineSkillMockMvc;

    private DisciplineSkill disciplineSkill;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSkill createEntity(EntityManager em) {
        DisciplineSkill disciplineSkill = new DisciplineSkill().skillModifier(DEFAULT_SKILL_MODIFIER);
        return disciplineSkill;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DisciplineSkill createUpdatedEntity(EntityManager em) {
        DisciplineSkill disciplineSkill = new DisciplineSkill().skillModifier(UPDATED_SKILL_MODIFIER);
        return disciplineSkill;
    }

    @BeforeEach
    public void initTest() {
        disciplineSkill = createEntity(em);
    }

    @Test
    @Transactional
    void createDisciplineSkill() throws Exception {
        int databaseSizeBeforeCreate = disciplineSkillRepository.findAll().size();
        // Create the DisciplineSkill
        restDisciplineSkillMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isCreated());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeCreate + 1);
        DisciplineSkill testDisciplineSkill = disciplineSkillList.get(disciplineSkillList.size() - 1);
        assertThat(testDisciplineSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void createDisciplineSkillWithExistingId() throws Exception {
        // Create the DisciplineSkill with an existing ID
        disciplineSkill.setId(1L);

        int databaseSizeBeforeCreate = disciplineSkillRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplineSkillMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDisciplineSkills() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        // Get all the disciplineSkillList
        restDisciplineSkillMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplineSkill.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillModifier").value(hasItem(DEFAULT_SKILL_MODIFIER)));
    }

    @Test
    @Transactional
    void getDisciplineSkill() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        // Get the disciplineSkill
        restDisciplineSkillMockMvc
            .perform(get(ENTITY_API_URL_ID, disciplineSkill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disciplineSkill.getId().intValue()))
            .andExpect(jsonPath("$.skillModifier").value(DEFAULT_SKILL_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingDisciplineSkill() throws Exception {
        // Get the disciplineSkill
        restDisciplineSkillMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDisciplineSkill() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();

        // Update the disciplineSkill
        DisciplineSkill updatedDisciplineSkill = disciplineSkillRepository.findById(disciplineSkill.getId()).get();
        // Disconnect from session so that the updates on updatedDisciplineSkill are not directly saved in db
        em.detach(updatedDisciplineSkill);
        updatedDisciplineSkill.skillModifier(UPDATED_SKILL_MODIFIER);

        restDisciplineSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDisciplineSkill.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDisciplineSkill))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSkill testDisciplineSkill = disciplineSkillList.get(disciplineSkillList.size() - 1);
        assertThat(testDisciplineSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disciplineSkill.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisciplineSkillWithPatch() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();

        // Update the disciplineSkill using partial update
        DisciplineSkill partialUpdatedDisciplineSkill = new DisciplineSkill();
        partialUpdatedDisciplineSkill.setId(disciplineSkill.getId());

        restDisciplineSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSkill))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSkill testDisciplineSkill = disciplineSkillList.get(disciplineSkillList.size() - 1);
        assertThat(testDisciplineSkill.getSkillModifier()).isEqualTo(DEFAULT_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateDisciplineSkillWithPatch() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();

        // Update the disciplineSkill using partial update
        DisciplineSkill partialUpdatedDisciplineSkill = new DisciplineSkill();
        partialUpdatedDisciplineSkill.setId(disciplineSkill.getId());

        partialUpdatedDisciplineSkill.skillModifier(UPDATED_SKILL_MODIFIER);

        restDisciplineSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisciplineSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisciplineSkill))
            )
            .andExpect(status().isOk());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
        DisciplineSkill testDisciplineSkill = disciplineSkillList.get(disciplineSkillList.size() - 1);
        assertThat(testDisciplineSkill.getSkillModifier()).isEqualTo(UPDATED_SKILL_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disciplineSkill.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isBadRequest());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisciplineSkill() throws Exception {
        int databaseSizeBeforeUpdate = disciplineSkillRepository.findAll().size();
        disciplineSkill.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisciplineSkillMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disciplineSkill))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DisciplineSkill in the database
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisciplineSkill() throws Exception {
        // Initialize the database
        disciplineSkillRepository.saveAndFlush(disciplineSkill);

        int databaseSizeBeforeDelete = disciplineSkillRepository.findAll().size();

        // Delete the disciplineSkill
        restDisciplineSkillMockMvc
            .perform(delete(ENTITY_API_URL_ID, disciplineSkill.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DisciplineSkill> disciplineSkillList = disciplineSkillRepository.findAll();
        assertThat(disciplineSkillList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
