package com.demo.eberber.specification;
/*
 * ContactRepository ayrıca JpaSpecificationExecutor <T> arayüzünü genişletir .
 *  Bu arayüz JPA Criteria API kullanarak veritabanı sorgularını çağırmak için kullanılabilecek 
 *  yöntemler sağlar. T, sorgulanan kuruluşun türünü açıklar, 
 *  bizim durumumuzda İletişim. 
 *  Çağrılan veritabanı sorgusunun koşullarını belirtmek için, 
 *  yeni bir Şartname uygulaması yaratmamız gerekiyor : <T> : ContactSpecification sınıfı.
 * */
import com.demo.eberber.domain.Customer;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
 
@SuppressWarnings("serial")
public class CustomerSpecification implements Specification<Customer> {
 
    private Customer filter;
 
    public CustomerSpecification(Customer filter) {
        super();
        this.filter = filter;
    }
 
    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> cq,
            CriteriaBuilder cb) {
 
        Predicate p = cb.disjunction();
 
        if (filter.getName() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getName() + "%"));
        }
 
        if (filter.getLastName()!= null) {
            p.getExpressions().add(cb.like(root.get("lastName"), "%" + filter.getLastName() + "%"));
        }
 
        if (filter.getAdress() != null) {
            p.getExpressions().add(cb.like(root.get("adress"), "%" + filter.getAdress() + "%"));
        }
 
        if (filter.geteMail()!= null) {
            p.getExpressions().add(cb.like(root.get("phone"), "%" + filter.geteMail() + "%"));
        }
        if (filter.getPhoneNo() != null) {
            p.getExpressions().add(cb.like(root.get("name"), "%" + filter.getPhoneNo() + "%"));
        }
 
        if (filter.getPassword()!= null) {
            p.getExpressions().add(cb.like(root.get("phone"), "%" + filter.getPassword() + "%"));
        }
        return p;
    }
}