package com.siwoo.application.repository;

import com.siwoo.application.domain.EmployeeSummary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("summaryRepository")
public class SummaryRepositoryImpl implements SummaryRepository {

    @PersistenceContext EntityManager entityManager;

    private static final String JPQL_SUMMARY
            = "select e.firstName, e.lastName, c.point " +
            "from Employee e left join e.cards c " +
            "where c.point = ( select max(c2.point) from Card c2 " +
            "where c2.owner.id = e.id )";
    @Override
    public void showSummary() {
        List<Object[]> results = entityManager.createQuery(JPQL_SUMMARY,Object[].class)
                .getResultList();

        for(Object[] rows: results){
            System.out.printf("FirstName: "+rows[0]+", ");
            System.out.printf("LastName: "+rows[1]+", ");
            System.out.printf("Point: "+rows[2]+"\n");
        }
    }

    private static final String JPQL_FIND_SUMMARY_ALL =
            "select new com.siwoo.application.domain.EmployeeSummary( " +
                    "e.firstName, e.lastName, c.point ) from Employee e " +
                    "left join e.cards c " +
                    "where c.point = (select max(c2.point) from Card c2 " +
                    "where c2.owner.id = e.id )";
    @Override
    public List<EmployeeSummary> findAll() {
        return entityManager.createQuery(JPQL_FIND_SUMMARY_ALL,EmployeeSummary.class)
                .getResultList();
    }
}
