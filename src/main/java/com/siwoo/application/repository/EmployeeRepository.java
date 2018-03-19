package com.siwoo.application.repository;

import com.siwoo.application.domain.Employee;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * The interface Employee repository.
 */
@Transactional(readOnly = true)
public interface EmployeeRepository {
    /**
     * Find all list.
     *
     * @return the list
     */
    List<Employee> findAll();

    /**
     * Find all with album list.
     *
     * @return the list
     */
    List<Employee> findAllWithCard();

    /**
     * Find by id singer.
     * @param id of employee*
     * @return the singer
     */
    Employee findById(long id);

    /**
     * Save employee.
     *
     * @param employee the employee
     * @return the employee
     */
    @Transactional(readOnly = false)
    Employee save(Employee employee);

    /**
     * Delete.
     *
     * @param employee the employee
     */
    @Transactional(readOnly = false)
    void delete(Employee employee);

    /**
     * Find all by native query list.
     *
     * @return the list
     */
    List<Employee> findAllByNativeQuery();

}
