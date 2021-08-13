package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class LeaveDeductionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveDeductionDTO.class);
        LeaveDeductionDTO leaveDeductionDTO1 = new LeaveDeductionDTO();
        leaveDeductionDTO1.setId(1L);
        LeaveDeductionDTO leaveDeductionDTO2 = new LeaveDeductionDTO();
        assertThat(leaveDeductionDTO1).isNotEqualTo(leaveDeductionDTO2);
        leaveDeductionDTO2.setId(leaveDeductionDTO1.getId());
        assertThat(leaveDeductionDTO1).isEqualTo(leaveDeductionDTO2);
        leaveDeductionDTO2.setId(2L);
        assertThat(leaveDeductionDTO1).isNotEqualTo(leaveDeductionDTO2);
        leaveDeductionDTO1.setId(null);
        assertThat(leaveDeductionDTO1).isNotEqualTo(leaveDeductionDTO2);
    }
}
