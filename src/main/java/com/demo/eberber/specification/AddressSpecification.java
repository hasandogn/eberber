package com.demo.eberber.specification;

import com.demo.eberber.domain.Address;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecification implements Specification<Address>{
    private Address filter;
    public AddressSpecification(Address filter) {
        super();
        this.filter = filter;
    }
    @Override
    public  Predicate toPredicate(Root<Address> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();

        if(filter.getAddressCity() != null) {
            p.getExpressions().add(cb.like(root.get("getAddressCity"), "%" + filter.getAddressCity()+ "%"));
        }
        if(filter.getAdresDetay() != null) {
            p.getExpressions().add(cb.like(root.get("getAddressDetay"), "%" + filter.getAdresDetay()+ "%"));
        }

        return p;
    }
}
