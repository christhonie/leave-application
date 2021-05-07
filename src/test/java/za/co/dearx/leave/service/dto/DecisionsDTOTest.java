package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class DecisionsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecisionsDTO.class);
        DecisionsDTO decisionsDTO1 = new DecisionsDTO();
        decisionsDTO1.setId(1L);
        DecisionsDTO decisionsDTO2 = new DecisionsDTO();
        assertThat(decisionsDTO1).isNotEqualTo(decisionsDTO2);
        decisionsDTO2.setId(decisionsDTO1.getId());
        assertThat(decisionsDTO1).isEqualTo(decisionsDTO2);
        decisionsDTO2.setId(2L);
        assertThat(decisionsDTO1).isNotEqualTo(decisionsDTO2);
        decisionsDTO1.setId(null);
        assertThat(decisionsDTO1).isNotEqualTo(decisionsDTO2);
    }
}
