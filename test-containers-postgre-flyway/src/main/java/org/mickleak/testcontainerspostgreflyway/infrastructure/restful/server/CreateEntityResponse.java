package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;

import org.mickleak.testcontainerspostgreflyway.domain.Entity;


public class CreateEntityResponse {
    private final String id;
    private final String name;


    public CreateEntityResponse(Entity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
