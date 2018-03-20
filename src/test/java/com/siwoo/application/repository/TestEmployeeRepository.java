package com.siwoo.application.repository;

import com.siwoo.application.config.JpaConfig;
import com.siwoo.application.domain.Card;
import com.siwoo.application.domain.Employee;
import com.siwoo.application.domain.EmployeeSummary;
import com.siwoo.application.domain.Employee_;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaConfig.class)
public class TestEmployeeRepository {

    @Autowired EmployeeRepository employeeRepository;

    private List<Employee> fixture_employees;
    @Before
    public void setup(){
        assertNotNull(employeeRepository);
        fixture_employees =
                Arrays.asList(
                    new Employee("first1","last1", LocalDate.now()),
                    new Employee("first2","last2", LocalDate.now()),
                    new Employee("first3","last3", LocalDate.now()),
                    new Employee("first4","last4", LocalDate.now()));

        Employee employee = fixture_employees.get(0);
        Card card1 = new Card(LocalDate.now(),10);
        //danger loss of card with same time with same point
        Card card2 = new Card(LocalDate.now(),11);
        employee.addCard(card1);
        employee.addCard(card2);


        employee = fixture_employees.get(1);
        card1 = new Card(LocalDate.now(),15);
        //danger loss of card with same time with same point
        card2 = new Card(LocalDate.now(),23);
        employee.addCard(card1);
        employee.addCard(card2);

        for(Employee e : fixture_employees){
            employeeRepository.save(e);
        }
    }

    Consumer<Employee> listEntity = entity -> {
        log.info(entity.toString());
    };
    Consumer<Collection<?>> listEntities = entities -> {
        entities.stream().map(Object::toString).forEach(log::info);
    };

    @Test
    public void testSave(){
        employeeRepository.save(fixture_employees.get(0));
        Employee foundEmployee = employeeRepository.findById(fixture_employees.get(0).getId());
        assertNotNull(foundEmployee);
        listEntity.accept(foundEmployee);
    }

    @Test
    public void testFindAll(){
        List<Employee> foundEmployees = employeeRepository.findAll();
        assertEquals(foundEmployees.size(),fixture_employees.size());
        listEntities.accept(foundEmployees);
    }

    @Test
    public void testFindAllWithCard(){
        List<Employee> foundEmployees = employeeRepository.findAllWithCard();
        assertEquals(foundEmployees.size(),fixture_employees.size());

        Employee employeeWithCard = foundEmployees.stream()
                .filter(employee -> employee.getId() == fixture_employees.get(0).getId())
                .findFirst().get();

        assertNotNull( employeeWithCard.getCards() );
        assertEquals(employeeWithCard.getCards().size() , 2);

        listEntities.accept(employeeWithCard.getCards());
    }


    @Autowired SummaryRepository summaryRepository;

    @Test
    public void testSummaryCard(){
        summaryRepository.showSummary();

        List<EmployeeSummary> summaries = summaryRepository.findAll();

        assertTrue(summaries.size() == 2);
        listEntities.accept(summaries);
    }

    @Test
    public void testUpdate(){
        Employee foundEmp = employeeRepository.findById(fixture_employees.get(0).getId());
        long id = foundEmp.getId();
        List<Employee> employeeList = employeeRepository.findAll();
        foundEmp = employeeList.stream().filter(emp -> emp.getId() == id).findFirst().get();
        String firstName = "Changed Name";
        foundEmp.setFirstName(firstName);
        employeeRepository.save(foundEmp);
        foundEmp = employeeRepository.findById(fixture_employees.get(0).getId());
        assertEquals(foundEmp.getFirstName(),firstName);
        assertNotNull(foundEmp.getCards());
        listEntities.accept(foundEmp.getCards());
    }

    @Test(expected = NoResultException.class)
    public void testRemove(){
        Employee foundEmp = employeeRepository.findById(fixture_employees.get(0).getId());
        employeeRepository.delete(foundEmp);
        assertNull( employeeRepository.findById(fixture_employees.get(0).getId()) );
    }

    @Test
    public void testFindCriteriaQuery(){
        Employee foundEmp = employeeRepository.findById(fixture_employees.get(0).getId());
        String firstName = foundEmp.getFirstName();
        String lastName = foundEmp.getLastName();

        List<Employee> employees = employeeRepository.findByCriteriaQuery(firstName,lastName);
        employees.stream().forEach(employee -> {
            assertEquals(employee.getFirstName(),firstName);
            assertEquals(employee.getLastName(),lastName);
            log.info(employee.toString());
        });
    }

    @Autowired CardRepository cardRepository;

    @Test
    public void testFindByPoint(){

        Card card = new Card();
        card.setPoint(10.0);
        card.setIssueDate(LocalDate.now());
        cardRepository.save(card);

        List<Card> foundCard = cardRepository.findByPoint(card.getPoint());

        assertNotNull(foundCard);
        assertEquals(foundCard.size(),2);

        listEntities.accept(foundCard);
    }
}


















