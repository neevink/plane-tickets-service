package se.ifmo.ru.firstservice.storage.repostitory.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.ifmo.ru.firstservice.exception.NotFoundException;
import se.ifmo.ru.firstservice.service.model.View;
import se.ifmo.ru.firstservice.storage.model.*;
import se.ifmo.ru.firstservice.storage.model.FlatEntity;
import se.ifmo.ru.firstservice.storage.repostitory.api.FlatRepository;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class FlatRepositoryImpl implements FlatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public FlatEntity findById(long id) {
        return entityManager.find(FlatEntity.class, id);
    }

    @Override
    @Transactional
    public FlatEntity save(FlatEntity entity) {
        if (entity == null){
            return null;
        }

        entity.setCreationDate(LocalDateTime.now());

        return entityManager.merge(entity);
    }

    @Override
    @Transactional
    public boolean deleteById(long id) {
        return entityManager.createQuery("DELETE FROM flat f WHERE f.id=:id")
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Page<FlatEntity> getSortedAndFilteredPage(List<Sort> sortList, List<Filter> filtersList, Integer page, Integer size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FlatEntity> flatQuery = criteriaBuilder.createQuery(FlatEntity.class);
        Root<FlatEntity> root = flatQuery.from(FlatEntity.class);

        CriteriaQuery<FlatEntity> select = flatQuery.select(root);

        List<Predicate> predicates = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(filtersList)){
            predicates = new ArrayList<>();

            for (Filter filter : filtersList){

                switch (filter.getFilteringOperation()){
                    case EQ:
                        if (filter.getNestedName() != null) {
                            predicates.add(criteriaBuilder.equal(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            getTypedFieldValue(filter.getFieldName(), filter.getFieldValue())
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.equal(
                                            root.get(filter.getFieldName()),
                                            getTypedFieldValue(filter.getFieldName(), filter.getFieldValue())
                                    )
                            );
                        }
                        break;
                    case NEQ:
                        if (filter.getNestedName() != null) {
                            predicates.add(criteriaBuilder.notEqual(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            getTypedFieldValue(filter.getFieldName(), filter.getFieldValue())
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.notEqual(
                                            root.get(filter.getFieldName()),
                                            getTypedFieldValue(filter.getFieldName(), filter.getFieldValue())
                                    )
                            );
                        }
                        break;
                    case GT:
                        if (filter.getNestedName() != null) {
                            predicates.add(criteriaBuilder.greaterThan(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            filter.getFieldValue()
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.greaterThan(
                                            root.get(filter.getFieldName()),
                                            filter.getFieldValue()
                                    )
                            );
                        }
                        break;
                    case LT:
                        if (filter.getNestedName() != null) {
                            predicates.add(criteriaBuilder.lessThan(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            filter.getFieldValue()
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.lessThan(
                                            root.get(filter.getFieldName()),
                                            filter.getFieldValue()
                                    )
                            );
                        }
                        break;
                    case GTE:
                        if (filter.getNestedName() != null) {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            filter.getFieldValue()
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                            root.get(filter.getFieldName()),
                                            filter.getFieldValue()
                                    )
                            );
                        }
                        break;
                    case LTE:
                        if (filter.getNestedName() != null){
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                            root.get(filter.getFieldName()).get(filter.getNestedName()),
                                            filter.getFieldValue()
                                    )
                            );
                        } else {
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                            root.get(filter.getFieldName()),
                                            filter.getFieldValue()
                                    )
                            );
                        }
                        break;
                    case UNDEFINED:
                        break;
                }
            }

            select.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        if (CollectionUtils.isNotEmpty(sortList)){
            List<Order> orderList = new ArrayList<>();

            for (Sort sortItem : sortList){
                if (sortItem.isDesc()){
                    if (sortItem.getNestedName() != null){
                        orderList.add(criteriaBuilder.desc(root.get(sortItem.getFieldName()).get(sortItem.getNestedName())));
                    } else {
                        orderList.add(criteriaBuilder.desc(root.get(sortItem.getFieldName())));
                    }
                } else {
                    if (sortItem.getNestedName() != null){
                        orderList.add(criteriaBuilder.asc(root.get(sortItem.getFieldName()).get(sortItem.getNestedName())));
                    } else {
                        orderList.add(criteriaBuilder.asc(root.get(sortItem.getFieldName())));
                    }
                }
            }
            select.orderBy(orderList);
        }

        TypedQuery<FlatEntity> typedQuery = entityManager.createQuery(select);

        Page<FlatEntity> ret = new Page<>();

        if (page != null && size != null){
            typedQuery.setFirstResult((page - 1) * size);
            typedQuery.setMaxResults(size);

            long countResult = 0;

            if (CollectionUtils.isNotEmpty(predicates)){
                Query queryTotal = entityManager.createQuery("SELECT COUNT(f.id) FROM flat f");
                countResult = (long) queryTotal.getSingleResult();
            } else {
                CriteriaBuilder qb = entityManager.getCriteriaBuilder();
                CriteriaQuery<Long> cq = qb.createQuery(Long.class);
                cq.select(qb.count(cq.from(FlatEntity.class)));
                cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
                countResult = entityManager.createQuery(cq).getSingleResult();
            }

            ret.setPage(page);
            ret.setPageSize(size);
            ret.setTotalPages((int) Math.ceil((countResult * 1.0) / size));
            ret.setTotalCount(countResult);
        }

        ret.setObjects(typedQuery.getResultList());

        return ret;
    }

    @Override
    @Transactional
    public boolean deleteByView(View view) {
        long id;

        try {
            id = (long) entityManager.createQuery("SELECT f.id FROM flat f WHERE f.view=:view")
                    .setParameter("view", view)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e){
            throw new NotFoundException("Not Found");
        }

        return entityManager.createQuery("DELETE FROM flat f WHERE f.id=:id")
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public double averageTimeToMetro() {
        Object result = entityManager.createQuery("SELECT AVG(f.timeToMetroOnFoot) FROM flat f").getSingleResult();

        if (result == null){
            throw new NotFoundException("Таблица пуста, не удалось подсчитать результат!");
        }

        return (double) result;
    }

    @Override
    public List<String> getUniqueView() {
        return entityManager.createQuery("SELECT DISTINCT f.view FROM flat f").getResultList();
    }

    private Object getTypedFieldValue(String fieldName, String fieldValue) {
        if (Objects.equals(fieldName, "balcony")) {
            return Boolean.valueOf(fieldValue);
        } else if (Objects.equals(fieldName, "view")) {
            return View.fromValue(fieldValue);
        } else return fieldValue;
    }
}
