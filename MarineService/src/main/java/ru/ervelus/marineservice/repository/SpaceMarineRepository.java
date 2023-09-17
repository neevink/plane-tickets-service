package ru.ervelus.marineservice.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import ru.egormit.library.SpaceMarine;
import ru.egormit.library.enums.AstartesCategory;
import ru.egormit.library.enums.MeleeWeapon;
import ru.egormit.library.enums.SortByType;
import ru.egormit.library.enums.SortOrder;
import ru.egormit.library.enums.Weapon;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpaceMarineRepository extends SimpleJpaRepository<SpaceMarine, Long> {
    private final EntityManager entityManager;

    public SpaceMarineRepository(EntityManager em) {
        super(SpaceMarine.class, em);
        this.entityManager = em;
    }

    public List<SpaceMarine> getAllPageable(Integer page, Integer limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> criteriaQuery = builder.createQuery(SpaceMarine.class);
        criteriaQuery.select(criteriaQuery.from(SpaceMarine.class));
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public Map<MeleeWeapon, Long> groupByMeleeAndCount() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.groupBy(root.get("meleeWeapon"));
        query.multiselect(root.get("meleeWeapon"), builder.count(root));
        Map<MeleeWeapon, Long> result = new HashMap<>();
        result.put(MeleeWeapon.POWER_FIST, 0L);
        result.put(MeleeWeapon.CHAIN_SWORD, 0L);
        result.put(MeleeWeapon.CHAIN_AXE, 0L);
        entityManager.createQuery(query).getResultStream().forEach((item) ->
                result.put((MeleeWeapon) item.get(0), (Long) item.get(1)));
        return result;
    }

    public List<SpaceMarine> getAllMatchingFieldsSortedPageable(
            Map<String, String> fieldToVal, SortByType type, SortOrder order, Integer page, Integer limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> query = builder.createQuery(SpaceMarine.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.select(root);
        if (order.equals(SortOrder.ASC)) {
            query.orderBy(builder.asc(root.get(type.name())));
        } else query.orderBy(builder.desc(root.get(type.name())));
        query.where(preparePredicatesFromFilter(builder, fieldToVal, root));
        return entityManager.createQuery(query)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<SpaceMarine> getAllMatchingFieldsPageable(Map<String, String> fieldToVal, Integer page, Integer limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> query = builder.createQuery(SpaceMarine.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.select(root);
        query.where(preparePredicatesFromFilter(builder, fieldToVal, root));
        return entityManager.createQuery(query)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<SpaceMarine> getAllByNamePageable(String name, Integer page, Integer limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> query = builder.createQuery(SpaceMarine.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.where(builder.like(root.get("name"), "%" + name + "%"));
        return entityManager.createQuery(query)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<SpaceMarine> getAllByHealthGreaterPageable(Long health, Integer page, Integer limit) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SpaceMarine> query = builder.createQuery(SpaceMarine.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.where(builder.greaterThan(root.get("health"), health));
        return entityManager.createQuery(query)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    private Predicate[] preparePredicatesFromFilter(CriteriaBuilder builder, Map<String, String> fieldToVal, Root<SpaceMarine> root) {
        List<Predicate> pridicates = new ArrayList<>();
        for (Map.Entry<String, String> entry : fieldToVal.entrySet()) {
            switch (entry.getKey()) {
                case "creationDate": {
                    pridicates.add(builder.equal(root.get(entry.getKey()), ZonedDateTime.parse(entry.getValue())));
                    break;
                }
                case "category": {
                    pridicates.add(builder.equal(root.get(entry.getKey()), AstartesCategory.valueOf(entry.getValue())));
                    break;
                }
                case "weaponType": {
                    pridicates.add(builder.equal(root.get(entry.getKey()), Weapon.valueOf(entry.getValue())));
                    break;
                }
                case "meleeWeapon": {
                    pridicates.add(builder.equal(root.get(entry.getKey()), MeleeWeapon.valueOf(entry.getValue())));
                    break;
                }
                default: {
                    pridicates.add(builder.equal(root.get(entry.getKey()), entry.getValue()));
                }
            }
        }
        return pridicates.toArray(new Predicate[0]);
    }
}
