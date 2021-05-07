package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveEntitlementMapperTest {

    private LeaveEntitlementMapper leaveEntitlementMapper;

    @BeforeEach
    public void setUp() {
        leaveEntitlementMapper = new LeaveEntitlementMapperImpl();
    }
}
