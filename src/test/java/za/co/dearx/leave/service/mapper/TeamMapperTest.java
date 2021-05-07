package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TeamMapperTest {
    private TeamMapper teamMapper;

    @BeforeEach
    public void setUp() {
        teamMapper = new TeamMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(teamMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(teamMapper.fromId(null)).isNull();
    }
}
