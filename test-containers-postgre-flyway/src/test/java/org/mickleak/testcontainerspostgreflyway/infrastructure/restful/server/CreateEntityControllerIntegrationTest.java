package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;

import org.junit.jupiter.api.Test;
import org.mickleak.testcontainerspostgreflyway.AbstractIntegrationTest;
import org.mickleak.testcontainerspostgreflyway.infrastructure.persistence.EntityDBO;
import org.mickleak.testcontainerspostgreflyway.infrastructure.persistence.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class CreateEntityControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityRepository entityRepository;

    @Test
    void createEntity_success() throws Exception {
        // Clear any existing data
        entityRepository.deleteAll();

        String requestBody = """
                {
                    "name": "Test Entity"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/entity")
                        .with(user("admin").password("password").authorities(() -> "entity_edit"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Entity"));

        // Verify that the entity was saved to the database
        Iterable<EntityDBO> entities = entityRepository.findAll();
        assertThat(entities).hasSize(1);
        EntityDBO savedEntity = entities.iterator().next();
        assertThat(savedEntity.getName()).isEqualTo("Test Entity");
    }

    @Test
    void createEntity_forbiddenWhenNoAuthority() throws Exception {
        String requestBody = """
                {
                    "name": "Test Entity"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/entity")
                        .with(user("user").password("password")) // No authority
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }
}