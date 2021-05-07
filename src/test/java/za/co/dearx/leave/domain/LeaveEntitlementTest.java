package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class LeaveEntitlementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveEntitlement.class);
        LeaveEntitlement leaveEntitlement1 = new LeaveEntitlement();
        leaveEntitlement1.setId(1L);
        LeaveEntitlement leaveEntitlement2 = new LeaveEntitlement();
        leaveEntitlement2.setId(leaveEntitlement1.getId());
        assertThat(leaveEntitlement1).isEqualTo(leaveEntitlement2);
        leaveEntitlement2.setId(2L);
        assertThat(leaveEntitlement1).isNotEqualTo(leaveEntitlement2);
        leaveEntitlement1.setId(null);
        assertThat(leaveEntitlement1).isNotEqualTo(leaveEntitlement2);
    }
}
