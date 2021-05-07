package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class LeaveStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveStatus.class);
        LeaveStatus leaveStatus1 = new LeaveStatus();
        leaveStatus1.setId(1L);
        LeaveStatus leaveStatus2 = new LeaveStatus();
        leaveStatus2.setId(leaveStatus1.getId());
        assertThat(leaveStatus1).isEqualTo(leaveStatus2);
        leaveStatus2.setId(2L);
        assertThat(leaveStatus1).isNotEqualTo(leaveStatus2);
        leaveStatus1.setId(null);
        assertThat(leaveStatus1).isNotEqualTo(leaveStatus2);
    }
}
