package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaveApplicationMapperTest {
    private LeaveApplicationMapper leaveApplicationMapper;

    @BeforeEach
    public void setUp() {
        leaveApplicationMapper = new LeaveApplicationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaveApplicationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaveApplicationMapper.fromId(null)).isNull();
    }
}
