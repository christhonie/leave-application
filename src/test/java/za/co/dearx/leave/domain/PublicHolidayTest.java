package za.co.dearx.leave.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import za.co.dearx.leave.web.rest.TestUtil;

class PublicHolidayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublicHoliday.class);
        PublicHoliday publicHoliday1 = new PublicHoliday();
        publicHoliday1.setId(1L);
        PublicHoliday publicHoliday2 = new PublicHoliday();
        publicHoliday2.setId(publicHoliday1.getId());
        assertThat(publicHoliday1).isEqualTo(publicHoliday2);
        publicHoliday2.setId(2L);
        assertThat(publicHoliday1).isNotEqualTo(publicHoliday2);
        publicHoliday1.setId(null);
        assertThat(publicHoliday1).isNotEqualTo(publicHoliday2);
    }
}
