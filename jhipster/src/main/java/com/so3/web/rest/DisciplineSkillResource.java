package com.so3.web.rest;

import com.so3.domain.DisciplineSkill;
import com.so3.repository.DisciplineSkillRepository;
import com.so3.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.so3.domain.DisciplineSkill}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DisciplineSkillResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineSkillResource.class);

    private static final String ENTITY_NAME = "disciplineSkill";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisciplineSkillRepository disciplineSkillRepository;

    public DisciplineSkillResource(DisciplineSkillRepository disciplineSkillRepository) {
        this.disciplineSkillRepository = disciplineSkillRepository;
    }

    /**
     * {@code POST  /discipline-skills} : Create a new disciplineSkill.
     *
     * @param disciplineSkill the disciplineSkill to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new disciplineSkill, or with status {@code 400 (Bad Request)} if the disciplineSkill has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/discipline-skills")
    public ResponseEntity<DisciplineSkill> createDisciplineSkill(@RequestBody DisciplineSkill disciplineSkill) throws URISyntaxException {
        log.debug("REST request to save DisciplineSkill : {}", disciplineSkill);
        if (disciplineSkill.getId() != null) {
            throw new BadRequestAlertException("A new disciplineSkill cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DisciplineSkill result = disciplineSkillRepository.save(disciplineSkill);
        return ResponseEntity
            .created(new URI("/api/discipline-skills/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /discipline-skills/:id} : Updates an existing disciplineSkill.
     *
     * @param id the id of the disciplineSkill to save.
     * @param disciplineSkill the disciplineSkill to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSkill,
     * or with status {@code 400 (Bad Request)} if the disciplineSkill is not valid,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSkill couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/discipline-skills/{id}")
    public ResponseEntity<DisciplineSkill> updateDisciplineSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisciplineSkill disciplineSkill
    ) throws URISyntaxException {
        log.debug("REST request to update DisciplineSkill : {}, {}", id, disciplineSkill);
        if (disciplineSkill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSkill.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DisciplineSkill result = disciplineSkillRepository.save(disciplineSkill);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSkill.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /discipline-skills/:id} : Partial updates given fields of an existing disciplineSkill, field will ignore if it is null
     *
     * @param id the id of the disciplineSkill to save.
     * @param disciplineSkill the disciplineSkill to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated disciplineSkill,
     * or with status {@code 400 (Bad Request)} if the disciplineSkill is not valid,
     * or with status {@code 404 (Not Found)} if the disciplineSkill is not found,
     * or with status {@code 500 (Internal Server Error)} if the disciplineSkill couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/discipline-skills/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DisciplineSkill> partialUpdateDisciplineSkill(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DisciplineSkill disciplineSkill
    ) throws URISyntaxException {
        log.debug("REST request to partial update DisciplineSkill partially : {}, {}", id, disciplineSkill);
        if (disciplineSkill.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, disciplineSkill.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineSkillRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DisciplineSkill> result = disciplineSkillRepository
            .findById(disciplineSkill.getId())
            .map(
                existingDisciplineSkill -> {
                    if (disciplineSkill.getSkillModifier() != null) {
                        existingDisciplineSkill.setSkillModifier(disciplineSkill.getSkillModifier());
                    }

                    return existingDisciplineSkill;
                }
            )
            .map(disciplineSkillRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, disciplineSkill.getId().toString())
        );
    }

    /**
     * {@code GET  /discipline-skills} : get all the disciplineSkills.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disciplineSkills in body.
     */
    @GetMapping("/discipline-skills")
    public List<DisciplineSkill> getAllDisciplineSkills() {
        log.debug("REST request to get all DisciplineSkills");
        return disciplineSkillRepository.findAll();
    }

    /**
     * {@code GET  /discipline-skills/:id} : get the "id" disciplineSkill.
     *
     * @param id the id of the disciplineSkill to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the disciplineSkill, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/discipline-skills/{id}")
    public ResponseEntity<DisciplineSkill> getDisciplineSkill(@PathVariable Long id) {
        log.debug("REST request to get DisciplineSkill : {}", id);
        Optional<DisciplineSkill> disciplineSkill = disciplineSkillRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(disciplineSkill);
    }

    /**
     * {@code DELETE  /discipline-skills/:id} : delete the "id" disciplineSkill.
     *
     * @param id the id of the disciplineSkill to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/discipline-skills/{id}")
    public ResponseEntity<Void> deleteDisciplineSkill(@PathVariable Long id) {
        log.debug("REST request to delete DisciplineSkill : {}", id);
        disciplineSkillRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
