package com.so3.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.so3.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invention.class);
        Invention invention1 = new Invention();
        invention1.setId(1L);
        Invention invention2 = new Invention();
        invention2.setId(invention1.getId());
        assertThat(invention1).isEqualTo(invention2);
        invention2.setId(2L);
        assertThat(invention1).isNotEqualTo(invention2);
        invention1.setId(null);
        assertThat(invention1).isNotEqualTo(invention2);
    }
}
