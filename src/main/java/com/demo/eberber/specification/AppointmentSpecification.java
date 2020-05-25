package com.demo.eberber.specification;

import com.demo.eberber.domain.Appointment;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class AppointmentSpecification implements Specification<Appointment> {
    private Appointment filter;

    public AppointmentSpecification(Appointment filter) {
        super();
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(Root<Appointment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Predicate p = cb.disjunction();

        if(filter.getAppointmentDate() != null) {
            p.getExpressions().add(cb.like(root.get("appointmentDate"), "%" + filter.getAppointmentDate() + "%"));
        }

        if(filter.getBarberId() != 0) {
            p.getExpressions().add(cb.like(root.get("barberId"), "%" + filter.getBarberId() + "%"));
        }
        if (filter.getServiceId() == null) {
            p.getExpressions().add(cb.like(root.get("serviceId"), "%" + filter.getServiceId() + "%"));
        }
        if(filter.getCustomerId() != 0) {
            p.getExpressions().add(cb.like(root.get("customerId"), "%" + filter.getServiceId() + "%"));
        }
        return p;
    }
}
