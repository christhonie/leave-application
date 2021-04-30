package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaveStatusMapperTest {
    private LeaveStatusMapper leaveStatusMapper;

    @BeforeEach
    public void setUp() {
        leaveStatusMapper = new LeaveStatusMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaveStatusMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaveStatusMapper.fromId(null)).isNull();
    }
}
