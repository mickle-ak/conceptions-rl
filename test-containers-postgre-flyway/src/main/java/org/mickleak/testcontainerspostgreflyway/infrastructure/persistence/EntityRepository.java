package org.mickleak.testcontainerspostgreflyway.infrastructure.persistence;


import org.springframework.data.repository.CrudRepository;

public interface EntityRepository extends CrudRepository<EntityDBO, String> {
}
