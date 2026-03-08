package org.mickleak.testcontainerspostgreflyway.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity(name = "entity")
@Table(name = "entity")
public class EntityDBO {
    @Id
    private String id;

    @Column
    private String name;


    public EntityDBO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected EntityDBO() {
        // for JPA
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
