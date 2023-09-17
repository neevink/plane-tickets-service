package ru.ervelus.marineservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.egormit.library.SpaceMarine;

@Repository
public interface SpaceMarineJpaRepository extends CrudRepository<SpaceMarine, Long> {
}
