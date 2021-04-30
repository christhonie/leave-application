package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

public class LeaveTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveTypeDTO.class);
        LeaveTypeDTO leaveTypeDTO1 = new LeaveTypeDTO();
        leaveTypeDTO1.setId(1L);
        LeaveTypeDTO leaveTypeDTO2 = new LeaveTypeDTO();
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
        leaveTypeDTO2.setId(leaveTypeDTO1.getId());
        assertThat(leaveTypeDTO1).isEqualTo(leaveTypeDTO2);
        leaveTypeDTO2.setId(2L);
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
        leaveTypeDTO1.setId(null);
        assertThat(leaveTypeDTO1).isNotEqualTo(leaveTypeDTO2);
    }
}
