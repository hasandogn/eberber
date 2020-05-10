package com.demo.eberber.specification;

import com.demo.eberber.domain.ServiceBarber;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ServiceBarberSpecification implements Specification<ServiceBarber> {
    private ServiceBarber filter;

    public ServiceBarberSpecification(ServiceBarber filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<ServiceBarber> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();

        if(filter.getName() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
        }

        if(filter.getTime() != 0) {
            p.getExpressions().add(cb.like(root.get("time"), "%" + filter.getTime() + "%"));
        }
        if (filter.getPrice() != 0) {
            p.getExpressions().add(cb.like(root.get("price"), "%" + filter.getPrice() + "%"));
        }
        return p;
    }
}
