package org.mickleak.testcontainerspostgreflyway.infrastructure.persistence;

import org.mickleak.testcontainerspostgreflyway.application.EntityStoragePort;
import org.mickleak.testcontainerspostgreflyway.domain.Entity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;


@Component
public class EntityStorageAdapter implements EntityStoragePort {

    private final EntityRepository entityRepository;


    public EntityStorageAdapter(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }


    @Override
    @Transactional(propagation = REQUIRED)
    public void save(Entity entity) {
        EntityDBO entityDBO = new EntityDBO(entity.getId(), entity.getName());
        entityRepository.save(entityDBO);
    }
}
