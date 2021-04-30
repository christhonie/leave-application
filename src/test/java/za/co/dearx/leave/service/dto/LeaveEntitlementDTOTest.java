package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

public class LeaveEntitlementDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveEntitlementDTO.class);
        LeaveEntitlementDTO leaveEntitlementDTO1 = new LeaveEntitlementDTO();
        leaveEntitlementDTO1.setId(1L);
        LeaveEntitlementDTO leaveEntitlementDTO2 = new LeaveEntitlementDTO();
        assertThat(leaveEntitlementDTO1).isNotEqualTo(leaveEntitlementDTO2);
        leaveEntitlementDTO2.setId(leaveEntitlementDTO1.getId());
        assertThat(leaveEntitlementDTO1).isEqualTo(leaveEntitlementDTO2);
        leaveEntitlementDTO2.setId(2L);
        assertThat(leaveEntitlementDTO1).isNotEqualTo(leaveEntitlementDTO2);
        leaveEntitlementDTO1.setId(null);
        assertThat(leaveEntitlementDTO1).isNotEqualTo(leaveEntitlementDTO2);
    }
}
