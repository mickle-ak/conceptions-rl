package org.mickleak.testcontainerspostgreflyway.application;

import org.mickleak.testcontainerspostgreflyway.domain.Entity;


public interface EntityStoragePort {

    void save(Entity entity);
}
