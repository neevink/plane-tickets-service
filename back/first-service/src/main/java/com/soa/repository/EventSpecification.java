package com.soa.repository;

import com.soa.error.ErrorDescriptions;
import com.soa.model.Event;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification implements Specification<Event> {
    private List<FilterCriteria> criteries;

    public EventSpecification(List<FilterCriteria> filterCriteria) {
        this.criteries = filterCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (FilterCriteria crit : criteries) {
            if(crit.getKey().equals("date") || crit.getKey().equals("eventType")){
                continue;
            }

            if (crit.getOperation().equalsIgnoreCase("gt")) {
                predicates.add(builder.greaterThan(
                        root.<String> get(crit.getKey()), crit.getValue().toString()));
            }
            else if (crit.getOperation().equalsIgnoreCase("lt")) {
                predicates.add(builder.lessThan(
                        root.<String> get(crit.getKey()), crit.getValue().toString()));
            }
            else if (crit.getOperation().equalsIgnoreCase("eq")) {
                if (root.get(crit.getKey()).getJavaType() == String.class) {
                    predicates.add(builder.like(
                            root.<String>get(crit.getKey()), "%" + crit.getValue() + "%"));
                } else {
                    predicates.add(builder.equal(root.get(crit.getKey()), crit.getValue()));
                }
            }
            else if (crit.getOperation().equalsIgnoreCase("ne")) {
                if (root.get(crit.getKey()).getJavaType() == String.class) {
                    predicates.add(builder.notLike(
                            root.<String>get(crit.getKey()), "%" + crit.getValue() + "%"));
                } else {
                    predicates.add(builder.notEqual(root.get(crit.getKey()), crit.getValue()));
                }
            }
            else {
                throw ErrorDescriptions.INCORRECT_FILTER.exception();
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
