package com.siwoo.application.repository;

import com.siwoo.application.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
