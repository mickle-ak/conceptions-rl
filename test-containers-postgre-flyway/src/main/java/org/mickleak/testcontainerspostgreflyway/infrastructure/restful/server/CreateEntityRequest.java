package org.mickleak.testcontainerspostgreflyway.infrastructure.restful.server;


public class CreateEntityRequest {
    private final String name;

    public CreateEntityRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
