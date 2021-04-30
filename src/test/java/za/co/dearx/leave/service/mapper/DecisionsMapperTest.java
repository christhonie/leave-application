package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DecisionsMapperTest {
    private DecisionsMapper decisionsMapper;

    @BeforeEach
    public void setUp() {
        decisionsMapper = new DecisionsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(decisionsMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(decisionsMapper.fromId(null)).isNull();
    }
}
