package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleMapperTest {
    private RoleMapper roleMapper;

    @BeforeEach
    public void setUp() {
        roleMapper = new RoleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(roleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(roleMapper.fromId(null)).isNull();
    }
}
