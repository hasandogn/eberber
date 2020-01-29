package com.demo.eberber.specification;

import com.demo.eberber.domain.Barber;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BarberSpecification implements Specification<Barber>{
    private Barber filter;

    public BarberSpecification(Barber filter){
        super();
        this.filter = filter;
    }
    @Override
    public Predicate toPredicate(Root<Barber> root,CriteriaQuery<?> cq, CriteriaBuilder cb){
        Predicate p = cb.disjunction();

        if(filter.getBarberName() != null) {

            p.getExpressions().add(cb.like(root.get("barberName"), "%" + filter.getBarberName()+ "%"));
        }

        if(filter.getPhoneNo()!= null) {
            p.getExpressions().add(cb.like(root.get("phoneNo"), "%" + filter.getPhoneNo() + "%"));
        }

        if(filter.getAdress()!=null) {
            p.getExpressions().add(cb.like(root.get("adress"), "%" + filter.getAdress() + "%"));
        }

        if(filter.getCity()!=null) {
            p.getExpressions().add(cb.like(root.get("city"), "%" + filter.getCity() + "%"));
        }

        if(filter.geteMail()!=null) {
            p.getExpressions().add(cb.like(root.get("eMail"), "%" + filter.geteMail() + "%"));
        }

        return p;
    }
}
