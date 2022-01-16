package com.so3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.so3.IntegrationTest;
import com.so3.domain.Invention;
import com.so3.repository.InventionRepository;
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
 * Integration tests for the {@link InventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_COST = 1;
    private static final Integer UPDATED_COST = 2;

    private static final Integer DEFAULT_DIFFICULTY = 1;
    private static final Integer UPDATED_DIFFICULTY = 2;

    private static final String ENTITY_API_URL = "/api/inventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventionRepository inventionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventionMockMvc;

    private Invention invention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invention createEntity(EntityManager em) {
        Invention invention = new Invention().name(DEFAULT_NAME).cost(DEFAULT_COST).difficulty(DEFAULT_DIFFICULTY);
        return invention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invention createUpdatedEntity(EntityManager em) {
        Invention invention = new Invention().name(UPDATED_NAME).cost(UPDATED_COST).difficulty(UPDATED_DIFFICULTY);
        return invention;
    }

    @BeforeEach
    public void initTest() {
        invention = createEntity(em);
    }

    @Test
    @Transactional
    void createInvention() throws Exception {
        int databaseSizeBeforeCreate = inventionRepository.findAll().size();
        // Create the Invention
        restInventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invention)))
            .andExpect(status().isCreated());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeCreate + 1);
        Invention testInvention = inventionList.get(inventionList.size() - 1);
        assertThat(testInvention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvention.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testInvention.getDifficulty()).isEqualTo(DEFAULT_DIFFICULTY);
    }

    @Test
    @Transactional
    void createInventionWithExistingId() throws Exception {
        // Create the Invention with an existing ID
        invention.setId(1L);

        int databaseSizeBeforeCreate = inventionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invention)))
            .andExpect(status().isBadRequest());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventions() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        // Get all the inventionList
        restInventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invention.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].difficulty").value(hasItem(DEFAULT_DIFFICULTY)));
    }

    @Test
    @Transactional
    void getInvention() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        // Get the invention
        restInventionMockMvc
            .perform(get(ENTITY_API_URL_ID, invention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invention.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST))
            .andExpect(jsonPath("$.difficulty").value(DEFAULT_DIFFICULTY));
    }

    @Test
    @Transactional
    void getNonExistingInvention() throws Exception {
        // Get the invention
        restInventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInvention() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();

        // Update the invention
        Invention updatedInvention = inventionRepository.findById(invention.getId()).get();
        // Disconnect from session so that the updates on updatedInvention are not directly saved in db
        em.detach(updatedInvention);
        updatedInvention.name(UPDATED_NAME).cost(UPDATED_COST).difficulty(UPDATED_DIFFICULTY);

        restInventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInvention))
            )
            .andExpect(status().isOk());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
        Invention testInvention = inventionList.get(inventionList.size() - 1);
        assertThat(testInvention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvention.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testInvention.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void putNonExistingInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invention.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invention)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventionWithPatch() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();

        // Update the invention using partial update
        Invention partialUpdatedInvention = new Invention();
        partialUpdatedInvention.setId(invention.getId());

        partialUpdatedInvention.cost(UPDATED_COST).difficulty(UPDATED_DIFFICULTY);

        restInventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvention))
            )
            .andExpect(status().isOk());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
        Invention testInvention = inventionList.get(inventionList.size() - 1);
        assertThat(testInvention.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInvention.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testInvention.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void fullUpdateInventionWithPatch() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();

        // Update the invention using partial update
        Invention partialUpdatedInvention = new Invention();
        partialUpdatedInvention.setId(invention.getId());

        partialUpdatedInvention.name(UPDATED_NAME).cost(UPDATED_COST).difficulty(UPDATED_DIFFICULTY);

        restInventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvention))
            )
            .andExpect(status().isOk());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
        Invention testInvention = inventionList.get(inventionList.size() - 1);
        assertThat(testInvention.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInvention.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testInvention.getDifficulty()).isEqualTo(UPDATED_DIFFICULTY);
    }

    @Test
    @Transactional
    void patchNonExistingInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invention.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invention))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvention() throws Exception {
        int databaseSizeBeforeUpdate = inventionRepository.findAll().size();
        invention.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invention))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invention in the database
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvention() throws Exception {
        // Initialize the database
        inventionRepository.saveAndFlush(invention);

        int databaseSizeBeforeDelete = inventionRepository.findAll().size();

        // Delete the invention
        restInventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, invention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invention> inventionList = inventionRepository.findAll();
        assertThat(inventionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
