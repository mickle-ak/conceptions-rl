package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mickleak.testcontainerspostgreflyway.application.CreateEntityUseCase;
import org.mickleak.testcontainerspostgreflyway.domain.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CreateEntityController.class)
@Import(org.mickleak.testcontainerspostgreflyway.config.SecurityConfig.class)
class CreateEntityControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateEntityUseCase createEntityUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", authorities = {"entity_edit"})
    void createEntity_success() throws Exception {
        // Given
        CreateEntityRequest request = new CreateEntityRequest("Test Name");
        Entity mockEntity = new Entity("123", "Test Name");

        when(createEntityUseCase.execute(anyString())).thenReturn(mockEntity);

        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Test Name"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"other_role"})
    void createEntity_forbiddenWhenNoAuthority() throws Exception {
        // Given
        CreateEntityRequest request = new CreateEntityRequest("Test Name");

        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createEntity_unauthorizedWhenNotAuthenticated() throws Exception {
        // Given
        CreateEntityRequest request = new CreateEntityRequest("Test Name");

        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"entity_edit"})
    void createEntity_badRequestWhenNameIsNull() throws Exception {
        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"entity_edit"})
    void createEntity_badRequestWhenNameIsEmpty() throws Exception {
        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"entity_edit"})
    void createEntity_badRequestWhenNameIsMissing() throws Exception {
        // When & Then
        mockMvc.perform(post("/entity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}