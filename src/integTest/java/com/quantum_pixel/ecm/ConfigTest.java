package com.quantum_pixel.ecm;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@Testcontainers
@ActiveProfiles("test")
public abstract class ConfigTest {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgresContainer = new
            PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1-alpine"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void execute() {
        getTableToTruncate().forEach(table -> {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
            jdbcTemplate.execute("ALTER SEQUENCE %s_id_seq RESTART".formatted(table));
        });
    }

    protected abstract List<String> getTableToTruncate();
}
