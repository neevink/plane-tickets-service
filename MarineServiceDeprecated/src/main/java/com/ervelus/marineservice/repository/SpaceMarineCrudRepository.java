package com.ervelus.marineservice.repository;

import ru.egormit.library.SpaceMarine;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class SpaceMarineCrudRepository {
    @Inject
    private EntityManagerProvider entityManagerProvider;

    public void save(SpaceMarine spaceMarine) {
        entityManagerProvider.getEntityManager().persist(spaceMarine);
        entityManagerProvider.getEntityManager().flush();
    }

    public SpaceMarine getById(Long id) {
        return entityManagerProvider.getEntityManager().find(SpaceMarine.class, id);
    }

    public List<SpaceMarine> getAllPageable(Integer page, Integer limit) {
        CriteriaBuilder builder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> criteriaQuery = builder.createQuery(SpaceMarine.class);
        criteriaQuery.select(criteriaQuery.from(SpaceMarine.class));
        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long countMarines() {
        CriteriaBuilder builder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        criteriaQuery.select(builder.count(criteriaQuery.from(SpaceMarine.class)));
        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery).getSingleResult();
    }

    public void deleteById(Long id) {
        CriteriaBuilder builder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaDelete<SpaceMarine> delete = builder.createCriteriaDelete(SpaceMarine.class);
        Root<SpaceMarine> root = delete.from(SpaceMarine.class);
        delete.where(builder.equal(root.get("id"), id));
        entityManagerProvider.getEntityManager().createQuery(delete).executeUpdate();
    }
}
