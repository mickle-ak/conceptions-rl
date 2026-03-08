package org.mickleak.testcontainerspostgreflyway.application;

import org.mickleak.testcontainerspostgreflyway.domain.Entity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class CreateEntityUseCase {

    private final EntityStoragePort entityStorage;


    public CreateEntityUseCase(EntityStoragePort entityStorage) {
        this.entityStorage = entityStorage;
    }


    public Entity execute(String name) {
        Entity e = new Entity(UUID.randomUUID().toString(), name);
        entityStorage.save(e);
        return e;
    }
}
