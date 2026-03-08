package org.mickleak.testcontainerspostgreflyway.domain;

public class Entity {
    private final String id;
    private final String name;

    public Entity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
