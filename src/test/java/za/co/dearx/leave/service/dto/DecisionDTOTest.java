package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class DecisionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecisionDTO.class);
        DecisionDTO decisionDTO1 = new DecisionDTO();
        decisionDTO1.setId(1L);
        DecisionDTO decisionDTO2 = new DecisionDTO();
        assertThat(decisionDTO1).isNotEqualTo(decisionDTO2);
        decisionDTO2.setId(decisionDTO1.getId());
        assertThat(decisionDTO1).isEqualTo(decisionDTO2);
        decisionDTO2.setId(2L);
        assertThat(decisionDTO1).isNotEqualTo(decisionDTO2);
        decisionDTO1.setId(null);
        assertThat(decisionDTO1).isNotEqualTo(decisionDTO2);
    }
}
