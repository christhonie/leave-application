package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

public class LeaveStatusDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveStatusDTO.class);
        LeaveStatusDTO leaveStatusDTO1 = new LeaveStatusDTO();
        leaveStatusDTO1.setId(1L);
        LeaveStatusDTO leaveStatusDTO2 = new LeaveStatusDTO();
        assertThat(leaveStatusDTO1).isNotEqualTo(leaveStatusDTO2);
        leaveStatusDTO2.setId(leaveStatusDTO1.getId());
        assertThat(leaveStatusDTO1).isEqualTo(leaveStatusDTO2);
        leaveStatusDTO2.setId(2L);
        assertThat(leaveStatusDTO1).isNotEqualTo(leaveStatusDTO2);
        leaveStatusDTO1.setId(null);
        assertThat(leaveStatusDTO1).isNotEqualTo(leaveStatusDTO2);
    }
}
