package com.siwoo.application.repository;

import com.siwoo.application.domain.Address;
import com.siwoo.application.domain.Member;
import com.siwoo.application.domain.Point;
import com.siwoo.application.domain.MemberSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class MemberRepositoryImpl implements MemberRepository {
    EntityManager entityManager;
    private static Class domainClass = Member.class;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Member save(Member member){
        Assert.notNull(member,"Member must not be null");
        if(member.getId() == null){
            if(member.getJoinDate() == null){
                member.setJoinDate(LocalDate.now());
            }
            this.entityManager.persist(member);
        }else{
            member.setUpdateDate(LocalDate.now());
            this.entityManager.merge(member);
        }
        return member;
    }

    private static final String FIND_BY_USERNAME = "select m from Member m where " +
            "m.username = :username ";
    @Override
    public List<Member> findByUsername(String username){
        return this.entityManager
                .createQuery(FIND_BY_USERNAME,domainClass)
                .setParameter("username",username)
                .getResultList();
    };

    private static final String FIND_ALL = "select m from Member m ";
    @Override
    public List<Member> findAll(){
        TypedQuery<Member> query = entityManager
                .createQuery(FIND_ALL,domainClass);
        return query.getResultList();
    }

    private static final String FIND_USERNAME_AND_AGE =
            "select m.username, m.age from Member m ";
    @Override
    public List<Object[]> findUsernameAndAge(){
        return entityManager.createQuery(FIND_USERNAME_AND_AGE,Object[].class)
                .getResultList();
    }

    private static final String FIND_BY_ID =
            "select m from Member m where m.id = :id ";
    @Override
    public Member findById(long id){
        Member member = null;
        try {
            member = entityManager.createQuery(FIND_BY_ID,Member.class)
                    .setParameter("id",id)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.info("No Result of id "+id);
        }
        return member;
    }

    private static final String FIND_BY_JOINDATE =
            "select m from Member m where m.joinDate = ?1 ";
    @Override
    public List<Member> findByJoinDate(LocalDate localDate){
        return entityManager.createQuery(FIND_BY_JOINDATE,domainClass)
                .setParameter(1,localDate)
                .getResultList();
    }

    private static final String FIND_POINT_BY_MEMBERID =
            "select m.point from Member m where m.id = :id ";
    @Override
    public Point findPointsById(long memberId){
        Point point = null;
        try{
            point = entityManager.createQuery(FIND_POINT_BY_MEMBERID,Point.class)
                    .setParameter("id",memberId)
                    .getSingleResult();
        }catch (NoResultException e){
            log.warn("No Result of memberId "+memberId);
        }
        return point;
    }

    private static final String FIND_ADDRESS_BY_ID =
            "select m.address from Member m where m.id = :id ";
    @Override
    public Address findAddressById(long memberId){
        return entityManager.createQuery(FIND_ADDRESS_BY_ID,Address.class)
                .setParameter("id",memberId)
                .getSingleResult();
    }

    private static final String FIND_AVG_POINTS =
            "select avg(p.points) from Point p";
    @Override
    public double findAvgPoints(){
        return entityManager.createQuery(FIND_AVG_POINTS,Double.class)
                .getSingleResult();
    }

    private static final String FIND_POINT_USERNAME_JOINDATE =
            "select m.point.points, m.username, m.joinDate " +
                    "from Member m ";
    @Override
    public List<Object[]> findPointAndUsernameAndJoinDate(){
        return entityManager.createQuery(FIND_POINT_USERNAME_JOINDATE,Object[].class)
                .getResultList();
    }


    /*
        public MemberSummary(String username, LocalDate joinDate, double point)
     */
    private static final String FIND_MEMBERSUMMARY =
            "select new com.siwoo.application.domain.MemberSummary( " +
                    "m.username, m.joinDate, m.point.points) " +
                    "from Member m ";
    @Override
    public List<MemberSummary> findMemberSummary(){
        return entityManager.createQuery(FIND_MEMBERSUMMARY,MemberSummary.class)
                .getResultList();
    }

    public static class SimplePaging {
        private int firstIndex;
        private int size;
        public SimplePaging(int firstIndex, int size) {
            Assert.isTrue(firstIndex >= 0 && size > 0,"Page values are not valid");
            this.firstIndex = firstIndex;
            this.size = size;
        }
    }

    @Override
    public List<Member> findAll(SimplePaging paging){
     TypedQuery<Member> query = entityManager.createQuery(FIND_ALL,domainClass);
     page(query, paging);
     return query.getResultList();
    }

    private static TypedQuery page(TypedQuery query, SimplePaging paging){
        query.setFirstResult(paging.firstIndex);
        query.setMaxResults(paging.size);
        return query;
    }

}



































