package com.so3.repository;

import com.so3.domain.DisciplineSkill;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DisciplineSkill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplineSkillRepository extends JpaRepository<DisciplineSkill, Long> {}
