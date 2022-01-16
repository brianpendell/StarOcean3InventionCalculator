package com.so3.web.rest;

import com.so3.domain.Discipline;
import com.so3.repository.DisciplineRepository;
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
 * REST controller for managing {@link com.so3.domain.Discipline}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DisciplineResource {

    private final Logger log = LoggerFactory.getLogger(DisciplineResource.class);

    private static final String ENTITY_NAME = "discipline";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DisciplineRepository disciplineRepository;

    public DisciplineResource(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    /**
     * {@code POST  /disciplines} : Create a new discipline.
     *
     * @param discipline the discipline to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new discipline, or with status {@code 400 (Bad Request)} if the discipline has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/disciplines")
    public ResponseEntity<Discipline> createDiscipline(@RequestBody Discipline discipline) throws URISyntaxException {
        log.debug("REST request to save Discipline : {}", discipline);
        if (discipline.getId() != null) {
            throw new BadRequestAlertException("A new discipline cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Discipline result = disciplineRepository.save(discipline);
        return ResponseEntity
            .created(new URI("/api/disciplines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /disciplines/:id} : Updates an existing discipline.
     *
     * @param id the id of the discipline to save.
     * @param discipline the discipline to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discipline,
     * or with status {@code 400 (Bad Request)} if the discipline is not valid,
     * or with status {@code 500 (Internal Server Error)} if the discipline couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/disciplines/{id}")
    public ResponseEntity<Discipline> updateDiscipline(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Discipline discipline
    ) throws URISyntaxException {
        log.debug("REST request to update Discipline : {}, {}", id, discipline);
        if (discipline.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, discipline.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Discipline result = disciplineRepository.save(discipline);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, discipline.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /disciplines/:id} : Partial updates given fields of an existing discipline, field will ignore if it is null
     *
     * @param id the id of the discipline to save.
     * @param discipline the discipline to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated discipline,
     * or with status {@code 400 (Bad Request)} if the discipline is not valid,
     * or with status {@code 404 (Not Found)} if the discipline is not found,
     * or with status {@code 500 (Internal Server Error)} if the discipline couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/disciplines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Discipline> partialUpdateDiscipline(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Discipline discipline
    ) throws URISyntaxException {
        log.debug("REST request to partial update Discipline partially : {}, {}", id, discipline);
        if (discipline.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, discipline.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!disciplineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Discipline> result = disciplineRepository
            .findById(discipline.getId())
            .map(
                existingDiscipline -> {
                    if (discipline.getName() != null) {
                        existingDiscipline.setName(discipline.getName());
                    }
                    if (discipline.getHelperDevice() != null) {
                        existingDiscipline.setHelperDevice(discipline.getHelperDevice());
                    }

                    return existingDiscipline;
                }
            )
            .map(disciplineRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, discipline.getId().toString())
        );
    }

    /**
     * {@code GET  /disciplines} : get all the disciplines.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of disciplines in body.
     */
    @GetMapping("/disciplines")
    public List<Discipline> getAllDisciplines() {
        log.debug("REST request to get all Disciplines");
        return disciplineRepository.findAll();
    }

    /**
     * {@code GET  /disciplines/:id} : get the "id" discipline.
     *
     * @param id the id of the discipline to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the discipline, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/disciplines/{id}")
    public ResponseEntity<Discipline> getDiscipline(@PathVariable Long id) {
        log.debug("REST request to get Discipline : {}", id);
        Optional<Discipline> discipline = disciplineRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(discipline);
    }

    /**
     * {@code DELETE  /disciplines/:id} : delete the "id" discipline.
     *
     * @param id the id of the discipline to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/disciplines/{id}")
    public ResponseEntity<Void> deleteDiscipline(@PathVariable Long id) {
        log.debug("REST request to delete Discipline : {}", id);
        disciplineRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
