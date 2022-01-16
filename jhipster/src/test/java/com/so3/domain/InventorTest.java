package com.so3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.so3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventor.class);
        Inventor inventor1 = new Inventor();
        inventor1.setId(1L);
        Inventor inventor2 = new Inventor();
        inventor2.setId(inventor1.getId());
        assertThat(inventor1).isEqualTo(inventor2);
        inventor2.setId(2L);
        assertThat(inventor1).isNotEqualTo(inventor2);
        inventor1.setId(null);
        assertThat(inventor1).isNotEqualTo(inventor2);
    }
}
