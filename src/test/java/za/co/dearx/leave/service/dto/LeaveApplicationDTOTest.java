package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class LeaveApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveApplicationDTO.class);
        LeaveApplicationDTO leaveApplicationDTO1 = new LeaveApplicationDTO();
        leaveApplicationDTO1.setId(1L);
        LeaveApplicationDTO leaveApplicationDTO2 = new LeaveApplicationDTO();
        assertThat(leaveApplicationDTO1).isNotEqualTo(leaveApplicationDTO2);
        leaveApplicationDTO2.setId(leaveApplicationDTO1.getId());
        assertThat(leaveApplicationDTO1).isEqualTo(leaveApplicationDTO2);
        leaveApplicationDTO2.setId(2L);
        assertThat(leaveApplicationDTO1).isNotEqualTo(leaveApplicationDTO2);
        leaveApplicationDTO1.setId(null);
        assertThat(leaveApplicationDTO1).isNotEqualTo(leaveApplicationDTO2);
    }
}
