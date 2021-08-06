package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class LeaveDeductionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaveDeduction.class);
        LeaveDeduction leaveDeduction1 = new LeaveDeduction();
        leaveDeduction1.setId(1L);
        LeaveDeduction leaveDeduction2 = new LeaveDeduction();
        leaveDeduction2.setId(leaveDeduction1.getId());
        assertThat(leaveDeduction1).isEqualTo(leaveDeduction2);
        leaveDeduction2.setId(2L);
        assertThat(leaveDeduction1).isNotEqualTo(leaveDeduction2);
        leaveDeduction1.setId(null);
        assertThat(leaveDeduction1).isNotEqualTo(leaveDeduction2);
    }
}
