package com.siwoo.application.bootstrap;

import com.siwoo.application.domain.Address;
import com.siwoo.application.domain.Member;
import com.siwoo.application.domain.MemberSummary;
import com.siwoo.application.domain.Point;
import com.siwoo.application.repository.MemberRepository;
import com.siwoo.application.repository.MemberRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
public class Chapter3Bootstrap {

    private static Consumer<Collection<?>> list = entities -> {
        entities.stream().map(Object::toString).forEach(log::info);
    };

    private static BiConsumer<Collection<Object[]>,Integer> listWithRows = (rows,colSize) -> {
        for(Object[] objects : rows){
            StringBuffer buffer = new StringBuffer();
            for(int i=0;i<colSize;i++){
                buffer.append(objects[i]+", ");
            }
            log.info(buffer.toString());
        }
    };

    private static Consumer<Object> listEntity = entity -> {
        log.info(entity.toString());
    };

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("testingunit1");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();

        MemberRepository memberRepository = new MemberRepositoryImpl(entityManager);
        try {
            tx.begin();

            Member member = new Member();
            member.setUsername("user1");
            member.setAge(20);
            memberRepository.save(member);

            List<Member> members = memberRepository.findByUsername(member.getUsername());
            Assert.isTrue(members.size()==1,"Member should be found");
            Assert.isTrue(member == members.get(0),"Must be same instance");
            list.accept(members);

            members = memberRepository.findAll();
            Assert.notNull(members,"must not null");
            Assert.isTrue(members.size()==1, "Must get all members");
            list.accept(members);

            member = new Member();
            member.setUsername("user2");
            member.setAge(30);
            memberRepository.save(member);

            List<Object[]> rows = memberRepository.findUsernameAndAge();
            listWithRows.accept(rows,2);

//            try{
//                member = memberRepository.findById(999l);
//            }catch (NoResultException e){
//                log.warn("NoResultException trigger");
//            }
            member = memberRepository.findById(999l);
            Assert.isNull(member,"Member must be null");

            members = memberRepository.findByJoinDate(LocalDate.now());
            Assert.isTrue(members.size()==2,"Must retrieved by LocalDate class ");
            list.accept(members);

            member = memberRepository.findById(1l);
            Point point = new Point();
            double pointValue = 99.99;
            point.setPoints(pointValue);
            point.setMember(member);
            memberRepository.save(member);

            point = memberRepository.findPointsById(member.getId());
            Assert.isTrue(point.getPoints() == pointValue );
            Assert.isTrue(point.getMember() == member );
            log.info(point.toString());

            member.setAddress(new Address("M21 SJ2","Altomantd Dr","Halmont"));
            memberRepository.save(member);
            Address address = memberRepository.findAddressById(member.getId());
            Assert.notNull(address,"Embedded Type (Collection of scala) must be retrieved");
            listEntity.accept(address);

            member = memberRepository.findById(2l);
            point = new Point();
            double pointValue2 = 22.22;
            point.setPoints(pointValue2);
            point.setMember(member);
            memberRepository.save(member);
            double pointAvg = memberRepository.findAvgPoints();
            Assert.isTrue(pointAvg == (pointValue+pointValue2)/2 ,"Avg must be found");
            listEntity.accept(pointAvg);

            listWithRows.accept(memberRepository.findPointAndUsernameAndJoinDate(),3);

            List<MemberSummary> summaries = memberRepository.findMemberSummary();
            Assert.notNull(summaries,"summaryList must not be null");
            Assert.isTrue(summaries.size() == 2,"SummarySize must be same as Member size");
            list.accept(summaries);

            MemberRepositoryImpl.SimplePaging paging = new MemberRepositoryImpl.SimplePaging(0,1);
            members = memberRepository.findAll(paging);
            Assert.isTrue(members.size()==1,"Paging must be performed");
            listEntity.accept(members);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            //throw e;
        }finally {
            entityManager.close();
        }

        entityManagerFactory.close();

    }
}
