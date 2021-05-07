package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveApplicationMapperTest {

    private LeaveApplicationMapper leaveApplicationMapper;

    @BeforeEach
    public void setUp() {
        leaveApplicationMapper = new LeaveApplicationMapperImpl();
    }
}
