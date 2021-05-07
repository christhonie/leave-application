package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaveTypeMapperTest {
    private LeaveTypeMapper leaveTypeMapper;

    @BeforeEach
    public void setUp() {
        leaveTypeMapper = new LeaveTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaveTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaveTypeMapper.fromId(null)).isNull();
    }
}
