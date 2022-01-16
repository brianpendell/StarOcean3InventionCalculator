package com.so3.repository;

import com.so3.domain.Inventor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Inventor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventorRepository extends JpaRepository<Inventor, Long> {}
