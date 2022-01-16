package com.so3.web.rest;

import com.so3.domain.Invention;
import com.so3.repository.InventionRepository;
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
 * REST controller for managing {@link com.so3.domain.Invention}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InventionResource {

    private final Logger log = LoggerFactory.getLogger(InventionResource.class);

    private static final String ENTITY_NAME = "invention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventionRepository inventionRepository;

    public InventionResource(InventionRepository inventionRepository) {
        this.inventionRepository = inventionRepository;
    }

    /**
     * {@code POST  /inventions} : Create a new invention.
     *
     * @param invention the invention to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invention, or with status {@code 400 (Bad Request)} if the invention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventions")
    public ResponseEntity<Invention> createInvention(@RequestBody Invention invention) throws URISyntaxException {
        log.debug("REST request to save Invention : {}", invention);
        if (invention.getId() != null) {
            throw new BadRequestAlertException("A new invention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Invention result = inventionRepository.save(invention);
        return ResponseEntity
            .created(new URI("/api/inventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /inventions/:id} : Updates an existing invention.
     *
     * @param id the id of the invention to save.
     * @param invention the invention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invention,
     * or with status {@code 400 (Bad Request)} if the invention is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventions/{id}")
    public ResponseEntity<Invention> updateInvention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Invention invention
    ) throws URISyntaxException {
        log.debug("REST request to update Invention : {}, {}", id, invention);
        if (invention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Invention result = inventionRepository.save(invention);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invention.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /inventions/:id} : Partial updates given fields of an existing invention, field will ignore if it is null
     *
     * @param id the id of the invention to save.
     * @param invention the invention to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invention,
     * or with status {@code 400 (Bad Request)} if the invention is not valid,
     * or with status {@code 404 (Not Found)} if the invention is not found,
     * or with status {@code 500 (Internal Server Error)} if the invention couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inventions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Invention> partialUpdateInvention(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Invention invention
    ) throws URISyntaxException {
        log.debug("REST request to partial update Invention partially : {}, {}", id, invention);
        if (invention.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invention.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Invention> result = inventionRepository
            .findById(invention.getId())
            .map(
                existingInvention -> {
                    if (invention.getName() != null) {
                        existingInvention.setName(invention.getName());
                    }
                    if (invention.getCost() != null) {
                        existingInvention.setCost(invention.getCost());
                    }
                    if (invention.getDifficulty() != null) {
                        existingInvention.setDifficulty(invention.getDifficulty());
                    }

                    return existingInvention;
                }
            )
            .map(inventionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invention.getId().toString())
        );
    }

    /**
     * {@code GET  /inventions} : get all the inventions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventions in body.
     */
    @GetMapping("/inventions")
    public List<Invention> getAllInventions() {
        log.debug("REST request to get all Inventions");
        return inventionRepository.findAll();
    }

    /**
     * {@code GET  /inventions/:id} : get the "id" invention.
     *
     * @param id the id of the invention to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invention, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventions/{id}")
    public ResponseEntity<Invention> getInvention(@PathVariable Long id) {
        log.debug("REST request to get Invention : {}", id);
        Optional<Invention> invention = inventionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(invention);
    }

    /**
     * {@code DELETE  /inventions/:id} : delete the "id" invention.
     *
     * @param id the id of the invention to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventions/{id}")
    public ResponseEntity<Void> deleteInvention(@PathVariable Long id) {
        log.debug("REST request to delete Invention : {}", id);
        inventionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
