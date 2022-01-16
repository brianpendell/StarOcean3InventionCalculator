package com.so3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.so3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisciplineSkillTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisciplineSkill.class);
        DisciplineSkill disciplineSkill1 = new DisciplineSkill();
        disciplineSkill1.setId(1L);
        DisciplineSkill disciplineSkill2 = new DisciplineSkill();
        disciplineSkill2.setId(disciplineSkill1.getId());
        assertThat(disciplineSkill1).isEqualTo(disciplineSkill2);
        disciplineSkill2.setId(2L);
        assertThat(disciplineSkill1).isNotEqualTo(disciplineSkill2);
        disciplineSkill1.setId(null);
        assertThat(disciplineSkill1).isNotEqualTo(disciplineSkill2);
    }
}
