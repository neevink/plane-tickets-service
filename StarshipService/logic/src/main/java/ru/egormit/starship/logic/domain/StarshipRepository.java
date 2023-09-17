package ru.egormit.starship.logic.domain;

import ru.egormit.library.StarShip;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Репозиторий доступа к сущности {@link StarShip}.
 *
 * @author Egor Mitrofanov.
 */
@Stateless
public class StarshipRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(StarShip starShip) {
        em.persist(starShip);
        em.flush();
    }

    public StarShip findById(Long id) {
        return em.find(StarShip.class, id);
    }

    public List<StarShip> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StarShip> criteriaQuery = builder.createQuery(StarShip.class);
        criteriaQuery.select(criteriaQuery.from(StarShip.class));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
