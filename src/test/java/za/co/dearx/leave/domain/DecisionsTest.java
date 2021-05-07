package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class DecisionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Decisions.class);
        Decisions decisions1 = new Decisions();
        decisions1.setId(1L);
        Decisions decisions2 = new Decisions();
        decisions2.setId(decisions1.getId());
        assertThat(decisions1).isEqualTo(decisions2);
        decisions2.setId(2L);
        assertThat(decisions1).isNotEqualTo(decisions2);
        decisions1.setId(null);
        assertThat(decisions1).isNotEqualTo(decisions2);
    }
}
