package com.siwoo.application.repository;

import com.siwoo.application.domain.Employee;
import com.siwoo.application.domain.Employee_;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * This is implementation of EmployeeRepository with JPA api
 */
@Repository("employeeRepository") @Slf4j
public class EmployeeRepositoryImpl implements EmployeeRepository {

    /**
     * The Entity manager.
     */
    @PersistenceContext EntityManager entityManager;
    private static final Class domainClass = Employee.class;

    @Override
    public List<Employee> findAll() {
        return entityManager.createNamedQuery(Employee.NAMED_SQL_FINDALL,domainClass)
                .getResultList();
    }

    @Override
    public List<Employee> findAllWithCard() {
        return entityManager.createNamedQuery(Employee.NAMED_SQL_FINDALL_WITH_ASSOCIATION,domainClass)
                .getResultList();
    }

    @Override
    public Employee findById(long id) {
        return entityManager.createNamedQuery(Employee.NAMED_SQL_FIND_BY_ID,Employee.class)
                .setParameter("id",id)
                .getSingleResult();
    }

    @Override
    public Employee save(Employee employee) {
        Assert.notNull(employee,"employee must not be empty");

        if(employee.getId() == null){
            log.info("Inserting Data");
            entityManager.persist(employee);
        }else{
            log.info("Updating Data");
            entityManager.merge(employee);
        }

        return employee;
    }

    @Override
    public void delete(Employee employee) {
        employee = entityManager.merge(employee);
        log.warn(employee.toString());
        entityManager.remove(employee);
    }

    @Override
    public List<Employee> findAllByNativeQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Employee> findByCriteriaQuery(String firstName, String lastName) {
         /*
         * CriteriaBuilder - Criteria, Query 을 생성
         * Used to construct criteria queries, compound selections ,
         * expressions, predicates, orderings.
         */
         CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        /*
         * CriteriaQuery - 쿼리의 기본틀을 건설
         * Specify the item that is to be returned in the query result.
         */
         CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
         /*
         * Root - From절을 정의한다 (where are the data from?).
         * A root type in the from clause.
         */
         Root<Employee> employeeRoot = query.from(Employee.class);
         employeeRoot.fetch(Employee_.cards,JoinType.LEFT);
         employeeRoot.fetch(Employee_.projects,JoinType.LEFT);

        // projection
        query.select(employeeRoot);

        // Building Criteria
        Predicate criteria = cb.conjunction();

        if(firstName != null){
            /* The type of a simple or compound predicate*/
            Predicate p = cb.equal(employeeRoot.get(Employee_.firstName),firstName);
            criteria = cb.and(criteria,p);
        }

        if(lastName != null){
            Predicate p = cb.equal(employeeRoot.get(Employee_.lastName),lastName);
            criteria = cb.and(criteria,p);
        }

        query.where(criteria);
        return entityManager.createQuery(query).getResultList();


    }
}
