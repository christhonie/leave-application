package za.co.dearx.leave.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntitlementValueMapperTest {
    private EntitlementValueMapper entitlementValueMapper;

    @BeforeEach
    public void setUp() {
        entitlementValueMapper = new EntitlementValueMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(entitlementValueMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(entitlementValueMapper.fromId(null)).isNull();
    }
}
