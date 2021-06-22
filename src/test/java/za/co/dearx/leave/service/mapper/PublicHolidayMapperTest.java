package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublicHolidayMapperTest {

    private PublicHolidayMapper publicHolidayMapper;

    @BeforeEach
    public void setUp() {
        publicHolidayMapper = new PublicHolidayMapperImpl();
    }
}
