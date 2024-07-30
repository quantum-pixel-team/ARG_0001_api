package com.quantum_pixel.arg;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class ConfigTest {

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgresContainer = new
            PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1-alpine"));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void execute() {
        if(getTableToTruncate() != null)
        getTableToTruncate().forEach(table -> {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
            jdbcTemplate.execute("ALTER SEQUENCE %s_id_seq RESTART".formatted(table));
        });
    }

    protected List<String> getTableToTruncate(){
        return Collections.emptyList();
    }
}
