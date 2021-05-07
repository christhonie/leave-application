package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class EntitlementValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntitlementValue.class);
        EntitlementValue entitlementValue1 = new EntitlementValue();
        entitlementValue1.setId(1L);
        EntitlementValue entitlementValue2 = new EntitlementValue();
        entitlementValue2.setId(entitlementValue1.getId());
        assertThat(entitlementValue1).isEqualTo(entitlementValue2);
        entitlementValue2.setId(2L);
        assertThat(entitlementValue1).isNotEqualTo(entitlementValue2);
        entitlementValue1.setId(null);
        assertThat(entitlementValue1).isNotEqualTo(entitlementValue2);
    }
}
