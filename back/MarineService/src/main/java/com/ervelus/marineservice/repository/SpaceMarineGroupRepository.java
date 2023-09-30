package com.ervelus.marineservice.repository;

import ru.egormit.library.SpaceMarine;
import ru.egormit.library.enums.MeleeWeapon;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class SpaceMarineGroupRepository {
    @Inject
    private EntityManagerProvider entityManagerProvider;

    public Map<MeleeWeapon, Long> groupByMeleeAndCount() {
        CriteriaBuilder builder = entityManagerProvider.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<SpaceMarine> root = query.from(SpaceMarine.class);
        query.groupBy(root.get("meleeWeapon"));
        query.multiselect(root.get("meleeWeapon"), builder.count(root));
        Map<MeleeWeapon, Long> result = new HashMap<>();
        result.put(MeleeWeapon.POWER_FIST, 0L);
        result.put(MeleeWeapon.CHAIN_SWORD, 0L);
        result.put(MeleeWeapon.CHAIN_AXE, 0L);
        entityManagerProvider.getEntityManager().createQuery(query).getResultStream().forEach((item) ->
                result.put((MeleeWeapon) item.get(0), (Long) item.get(1)));
        return result;
    }
}
