package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LeaveEntitlementMapperTest {
    private LeaveEntitlementMapper leaveEntitlementMapper;

    @BeforeEach
    public void setUp() {
        leaveEntitlementMapper = new LeaveEntitlementMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(leaveEntitlementMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(leaveEntitlementMapper.fromId(null)).isNull();
    }
}
