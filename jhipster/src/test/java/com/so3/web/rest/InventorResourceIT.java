package com.so3.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.so3.IntegrationTest;
import com.so3.domain.Inventor;
import com.so3.repository.InventorRepository;
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
 * Integration tests for the {@link InventorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME_MODIFER = 1;
    private static final Integer UPDATED_TIME_MODIFER = 2;

    private static final Integer DEFAULT_COST_MODIFIER = 1;
    private static final Integer UPDATED_COST_MODIFIER = 2;

    private static final String ENTITY_API_URL = "/api/inventors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventorRepository inventorRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventorMockMvc;

    private Inventor inventor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventor createEntity(EntityManager em) {
        Inventor inventor = new Inventor().name(DEFAULT_NAME).timeModifer(DEFAULT_TIME_MODIFER).costModifier(DEFAULT_COST_MODIFIER);
        return inventor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventor createUpdatedEntity(EntityManager em) {
        Inventor inventor = new Inventor().name(UPDATED_NAME).timeModifer(UPDATED_TIME_MODIFER).costModifier(UPDATED_COST_MODIFIER);
        return inventor;
    }

    @BeforeEach
    public void initTest() {
        inventor = createEntity(em);
    }

    @Test
    @Transactional
    void createInventor() throws Exception {
        int databaseSizeBeforeCreate = inventorRepository.findAll().size();
        // Create the Inventor
        restInventorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventor)))
            .andExpect(status().isCreated());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeCreate + 1);
        Inventor testInventor = inventorList.get(inventorList.size() - 1);
        assertThat(testInventor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventor.getTimeModifer()).isEqualTo(DEFAULT_TIME_MODIFER);
        assertThat(testInventor.getCostModifier()).isEqualTo(DEFAULT_COST_MODIFIER);
    }

    @Test
    @Transactional
    void createInventorWithExistingId() throws Exception {
        // Create the Inventor with an existing ID
        inventor.setId(1L);

        int databaseSizeBeforeCreate = inventorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventor)))
            .andExpect(status().isBadRequest());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInventors() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        // Get all the inventorList
        restInventorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].timeModifer").value(hasItem(DEFAULT_TIME_MODIFER)))
            .andExpect(jsonPath("$.[*].costModifier").value(hasItem(DEFAULT_COST_MODIFIER)));
    }

    @Test
    @Transactional
    void getInventor() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        // Get the inventor
        restInventorMockMvc
            .perform(get(ENTITY_API_URL_ID, inventor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.timeModifer").value(DEFAULT_TIME_MODIFER))
            .andExpect(jsonPath("$.costModifier").value(DEFAULT_COST_MODIFIER));
    }

    @Test
    @Transactional
    void getNonExistingInventor() throws Exception {
        // Get the inventor
        restInventorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInventor() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();

        // Update the inventor
        Inventor updatedInventor = inventorRepository.findById(inventor.getId()).get();
        // Disconnect from session so that the updates on updatedInventor are not directly saved in db
        em.detach(updatedInventor);
        updatedInventor.name(UPDATED_NAME).timeModifer(UPDATED_TIME_MODIFER).costModifier(UPDATED_COST_MODIFIER);

        restInventorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInventor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInventor))
            )
            .andExpect(status().isOk());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
        Inventor testInventor = inventorList.get(inventorList.size() - 1);
        assertThat(testInventor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventor.getTimeModifer()).isEqualTo(UPDATED_TIME_MODIFER);
        assertThat(testInventor.getCostModifier()).isEqualTo(UPDATED_COST_MODIFIER);
    }

    @Test
    @Transactional
    void putNonExistingInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventor.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inventor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inventor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventorWithPatch() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();

        // Update the inventor using partial update
        Inventor partialUpdatedInventor = new Inventor();
        partialUpdatedInventor.setId(inventor.getId());

        partialUpdatedInventor.timeModifer(UPDATED_TIME_MODIFER).costModifier(UPDATED_COST_MODIFIER);

        restInventorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventor))
            )
            .andExpect(status().isOk());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
        Inventor testInventor = inventorList.get(inventorList.size() - 1);
        assertThat(testInventor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventor.getTimeModifer()).isEqualTo(UPDATED_TIME_MODIFER);
        assertThat(testInventor.getCostModifier()).isEqualTo(UPDATED_COST_MODIFIER);
    }

    @Test
    @Transactional
    void fullUpdateInventorWithPatch() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();

        // Update the inventor using partial update
        Inventor partialUpdatedInventor = new Inventor();
        partialUpdatedInventor.setId(inventor.getId());

        partialUpdatedInventor.name(UPDATED_NAME).timeModifer(UPDATED_TIME_MODIFER).costModifier(UPDATED_COST_MODIFIER);

        restInventorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInventor))
            )
            .andExpect(status().isOk());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
        Inventor testInventor = inventorList.get(inventorList.size() - 1);
        assertThat(testInventor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventor.getTimeModifer()).isEqualTo(UPDATED_TIME_MODIFER);
        assertThat(testInventor.getCostModifier()).isEqualTo(UPDATED_COST_MODIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inventor))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventor() throws Exception {
        int databaseSizeBeforeUpdate = inventorRepository.findAll().size();
        inventor.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inventor)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventor in the database
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventor() throws Exception {
        // Initialize the database
        inventorRepository.saveAndFlush(inventor);

        int databaseSizeBeforeDelete = inventorRepository.findAll().size();

        // Delete the inventor
        restInventorMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inventor> inventorList = inventorRepository.findAll();
        assertThat(inventorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
