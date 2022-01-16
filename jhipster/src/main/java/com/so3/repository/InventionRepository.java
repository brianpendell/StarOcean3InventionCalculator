package com.so3.repository;

import com.so3.domain.Invention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Invention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventionRepository extends JpaRepository<Invention, Long> {}
