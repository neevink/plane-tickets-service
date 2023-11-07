package com.soa.repository;

import com.soa.error.ErrorDescriptions;
import com.soa.model.Ticket;
import com.soa.model.enums.TicketType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TicketSpecification implements Specification<Ticket> {
    private List<FilterCriteria> criteries;

    public TicketSpecification(List<FilterCriteria> filterCriteria) {
        this.criteries = filterCriteria;
    }


    private String hackEnums(String maybeEnum) {
        if ("usual".equals(maybeEnum) || "budgetary".equals(maybeEnum) || "cheap".equals(maybeEnum) || "vip".equals(maybeEnum)
                || "concert".equals(maybeEnum) || "baseball".equals(maybeEnum) || "basketball".equals(maybeEnum)
                || "theatre_performance".equals(maybeEnum)){
            return maybeEnum.toUpperCase();
        }
        return maybeEnum;
    }

    @Override
    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (FilterCriteria crit : criteries) {
            if(crit.getKey().equals("creationDate") || crit.getKey().equals("type")){
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
                    if (root.get(crit.getKey()).getJavaType() == TicketType.class) {
                        predicates.add(builder.equal(root.get(crit.getKey()),
                                crit.getValue()));
                    }
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

