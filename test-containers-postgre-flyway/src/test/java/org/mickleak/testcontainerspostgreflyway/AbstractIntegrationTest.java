package org.mickleak.testcontainerspostgreflyway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("concept.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("concept.datasource.username", postgresqlContainer::getUsername);
        registry.add("concept.datasource.password", postgresqlContainer::getPassword);
        registry.add("concept.datasource.driver-class-name", postgresqlContainer::getDriverClassName);
        registry.add("concept.jpa.properties.hibernate.default_schema", () -> "concept");
        registry.add("concept.jpa.hibernate.ddl-auto", () -> "validate");
        
        // Enable Flyway auto-migration for integration tests
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.schemas", () -> "concept");
    }
}