package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateEntityRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private final String name;

    public CreateEntityRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}