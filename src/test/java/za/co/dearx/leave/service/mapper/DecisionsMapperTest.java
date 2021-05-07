package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionsMapperTest {

    private DecisionsMapper decisionsMapper;

    @BeforeEach
    public void setUp() {
        decisionsMapper = new DecisionsMapperImpl();
    }
}
