package com.so3.web.rest;

import com.so3.domain.Inventor;
import com.so3.repository.InventorRepository;
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
 * REST controller for managing {@link com.so3.domain.Inventor}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InventorResource {

    private final Logger log = LoggerFactory.getLogger(InventorResource.class);

    private static final String ENTITY_NAME = "inventor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventorRepository inventorRepository;

    public InventorResource(InventorRepository inventorRepository) {
        this.inventorRepository = inventorRepository;
    }

    /**
     * {@code POST  /inventors} : Create a new inventor.
     *
     * @param inventor the inventor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventor, or with status {@code 400 (Bad Request)} if the inventor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventors")
    public ResponseEntity<Inventor> createInventor(@RequestBody Inventor inventor) throws URISyntaxException {
        log.debug("REST request to save Inventor : {}", inventor);
        if (inventor.getId() != null) {
            throw new BadRequestAlertException("A new inventor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inventor result = inventorRepository.save(inventor);
        return ResponseEntity
            .created(new URI("/api/inventors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inventors/:id} : Updates an existing inventor.
     *
     * @param id the id of the inventor to save.
     * @param inventor the inventor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventor,
     * or with status {@code 400 (Bad Request)} if the inventor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventors/{id}")
    public ResponseEntity<Inventor> updateInventor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inventor inventor
    ) throws URISyntaxException {
        log.debug("REST request to update Inventor : {}, {}", id, inventor);
        if (inventor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inventor result = inventorRepository.save(inventor);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventor.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inventors/:id} : Partial updates given fields of an existing inventor, field will ignore if it is null
     *
     * @param id the id of the inventor to save.
     * @param inventor the inventor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventor,
     * or with status {@code 400 (Bad Request)} if the inventor is not valid,
     * or with status {@code 404 (Not Found)} if the inventor is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inventors/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Inventor> partialUpdateInventor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Inventor inventor
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inventor partially : {}, {}", id, inventor);
        if (inventor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inventor> result = inventorRepository
            .findById(inventor.getId())
            .map(
                existingInventor -> {
                    if (inventor.getName() != null) {
                        existingInventor.setName(inventor.getName());
                    }
                    if (inventor.getTimeModifer() != null) {
                        existingInventor.setTimeModifer(inventor.getTimeModifer());
                    }
                    if (inventor.getCostModifier() != null) {
                        existingInventor.setCostModifier(inventor.getCostModifier());
                    }

                    return existingInventor;
                }
            )
            .map(inventorRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventor.getId().toString())
        );
    }

    /**
     * {@code GET  /inventors} : get all the inventors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventors in body.
     */
    @GetMapping("/inventors")
    public List<Inventor> getAllInventors() {
        log.debug("REST request to get all Inventors");
        return inventorRepository.findAll();
    }

    /**
     * {@code GET  /inventors/:id} : get the "id" inventor.
     *
     * @param id the id of the inventor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventors/{id}")
    public ResponseEntity<Inventor> getInventor(@PathVariable Long id) {
        log.debug("REST request to get Inventor : {}", id);
        Optional<Inventor> inventor = inventorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(inventor);
    }

    /**
     * {@code DELETE  /inventors/:id} : delete the "id" inventor.
     *
     * @param id the id of the inventor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventors/{id}")
    public ResponseEntity<Void> deleteInventor(@PathVariable Long id) {
        log.debug("REST request to delete Inventor : {}", id);
        inventorRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
