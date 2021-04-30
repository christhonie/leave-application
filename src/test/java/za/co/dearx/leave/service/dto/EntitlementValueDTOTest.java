package za.co.dearx.leave.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

public class EntitlementValueDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntitlementValueDTO.class);
        EntitlementValueDTO entitlementValueDTO1 = new EntitlementValueDTO();
        entitlementValueDTO1.setId(1L);
        EntitlementValueDTO entitlementValueDTO2 = new EntitlementValueDTO();
        assertThat(entitlementValueDTO1).isNotEqualTo(entitlementValueDTO2);
        entitlementValueDTO2.setId(entitlementValueDTO1.getId());
        assertThat(entitlementValueDTO1).isEqualTo(entitlementValueDTO2);
        entitlementValueDTO2.setId(2L);
        assertThat(entitlementValueDTO1).isNotEqualTo(entitlementValueDTO2);
        entitlementValueDTO1.setId(null);
        assertThat(entitlementValueDTO1).isNotEqualTo(entitlementValueDTO2);
    }
}
