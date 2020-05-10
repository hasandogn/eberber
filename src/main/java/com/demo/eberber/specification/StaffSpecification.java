package com.demo.eberber.specification;

import com.demo.eberber.domain.Staff;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
public class StaffSpecification  implements Specification<Staff>{
    private Staff filter;

    public StaffSpecification(Staff filter) {
        super();
        this.filter = filter;
    }
    @Override
    public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();

        if (filter.getBarberId() != 0) {
            p.getExpressions().add(cb.like(root.get("lastName"), "%" + filter.getBarberId() + "%"));
        }

        if (filter.getStaffName() != null) {
            p.getExpressions().add(cb.like(root.get("adress"), "%" + filter.getStaffName() + "%"));
        }
        return p;
    }
}
