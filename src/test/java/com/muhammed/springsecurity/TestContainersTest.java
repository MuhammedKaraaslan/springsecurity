package com.muhammed.springsecurity;

import com.muhammed.springsecurity.abstracts.AbstractTestcontainers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContainersTest extends AbstractTestcontainers {
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
