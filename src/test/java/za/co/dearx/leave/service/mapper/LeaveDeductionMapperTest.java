package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveDeductionMapperTest {

    private LeaveDeductionMapper leaveDeductionMapper;

    @BeforeEach
    public void setUp() {
        leaveDeductionMapper = new LeaveDeductionMapperImpl();
    }
}
