package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class DecisionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Decision.class);
        Decision decision1 = new Decision();
        decision1.setId(1L);
        Decision decision2 = new Decision();
        decision2.setId(decision1.getId());
        assertThat(decision1).isEqualTo(decision2);
        decision2.setId(2L);
        assertThat(decision1).isNotEqualTo(decision2);
        decision1.setId(null);
        assertThat(decision1).isNotEqualTo(decision2);
    }
}
